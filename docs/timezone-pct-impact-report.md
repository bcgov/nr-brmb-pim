# BC Permanent Pacific Time (PCT, UTC-7): Impact Report for nr-brmb-pim

| | |
|---|---|
| **Prepared** | 2026-10-07 |
| **Repo state** | public GitHub `dhlevi/nr-brmb-pim`, branch `master` @ `a25cf332` (2026-08-14, "Merge pull request #517 from bcgov/release/4.1.1"). This commit is identical to `master` in the upstream `bcgov/nr-brmb-pim` repository. |
| **Components** | `cirras-underwriting-api` (Java 21, Spring, MyBatis, PostgreSQL, JasperReports 6.20.0; 439 main Java files, 20 `.jrxml` reports), `cirras-underwriting-war` (Angular 19, Angular Material with `MomentDateAdapter`, `moment` 2.30.1), `cirras-underwriting-ngclient-lib`, `cirras-underwriting-liquibase` (629 `date`, 277 `timestamp` and 1 `timestamptz` columns), `crunchy-postgres` (Crunchy PostgreSQL 17), `openshift` and `.github/workflows` |
| **Deadline** | **Sunday 2026-11-01, 02:00 local (09:00 UTC)**, about 3.5 weeks away |
| **Bottom line** | **High risk on the platform, low risk in the code.** This is the underwriting counterpart of `nr-brmb-pit-claim` and shares its design and its main risk. The code contains no fixed PDT or PST offsets and runs in `America/Vancouver`, but both containers are pinned to `tomcat:10.1.44-jre21` (August 2025), whose JRE predates tzdata 2026b. **From Nov 1 that JRE applies PST (UTC-8)**, so calendar dates that users pick in the browser (seeding and seeded dates, coverage deadlines, declaration of production dates, bog mowed and renovated dates) and dates synced from CIRRAS will be **stored one day early** (U1). Database audit times can be one hour off if PostgreSQL and the JRE disagree (U2). **Fix: move to a Tomcat image on JRE 21.0.12 or later, set `-Duser.timezone=America/Vancouver`, confirm the Crunchy PostgreSQL image carries tzdata 2026b, and patch in the same window as CIRRAS.** |

---

## 1. What changed (same basis as the earlier reports)

- **Government rule.** BC stopped changing clocks after 2026-03-08. On **2026-11-01 clocks do not fall back.** BC stays at **UTC-7** all year, named *Pacific time (PCT)*.
- **IANA tzdata 2026b** models `America/Vancouver` as permanent UTC-7 from 2026-11-01 02:00.
- **JDK builds with the rule:** 8u501, 11.0.32, 17.0.20, 21.0.12 and 25.0.4 or later. Earlier builds treat BC winter as PST (UTC-8).
- **moment-timezone ships its own tz data.** Only 0.6.2 and later contain the BC rule.

---

## 2. Summary of findings

| # | Area | Severity | Fails on Nov 1? | Fix |
|---|---|---|---|---|
| U1 | Calendar dates (user-entered and CIRRAS-synced) converted to PostgreSQL `date` columns through a stale JRE | **High** | **Yes**, if the JRE is not updated: browser dates sent as local midnight (`07:00Z`) are stored as the previous day | R1, R2, R4 |
| U2 | `now()` / `CURRENT_TIMESTAMP` audit values (60 mappers) in `timestamp` columns, read through the JVM zone | **Medium** | If the PostgreSQL image and the JRE carry different tzdata: stored one hour off | R1, R3 |
| U3 | JasperReports fill and format in the JVM zone; `now() as date_printed` from the database | Low | Printed dates and times one hour off, or one day off near midnight, on an unpatched JRE or database | R1, R3 |
| U4 | Optimistic sync ordering on wall-clock `*DataSyncTransDate` columns (43 bindings) | Low | Only during a mixed or late-patched period | R5 |
| U5 | Outbox polling first-run time in `ZoneId.systemDefault()` | Low | Starts one hour off on an unpatched JRE | R1 |
| U6 | Active and expiry calculations in `CirrasDataSyncRsrcFactory` and `LandDataSyncRsrcFactory` | Low | Only within one hour of an expiry boundary | R1 |
| U7 | Front end (`MomentDateAdapter` in local time, `moment` 2.30.1; `moment-timezone` 0.6.0 installed but unused) | Low | Only for users whose browser has outdated tz data | R6 |
| U8 | Fail-over ownership (`timestamptz` compared in UTC) | None | No | n/a |
| U9 | Crunchy backup schedule in UTC (comment assumes 08:00 UTC is midnight) | None | No; backups start at 01:00 local all year | R7 |
| D1 | `AsyncMasterTask` random start offset discarded (pre-existing) | Low | Not related to the change | R8 |
| D2 | `getMaxExpiryDate()` returns 9001-01-31 at the current time, in two factories (pre-existing) | Medium | Not related to the change | R8 |
| D3 | Fail-over log prints the UTC expiry as if it were local time (pre-existing) | Low | Not related to the change | R8 |

---

## 3. Overview

Both containers use `tomcat:10.1.44-jre21`, link `/etc/localtime` to `America/Vancouver` (`cirras-underwriting-api/Dockerfile` line 25, `cirras-underwriting-war/Dockerfile` line 22), and the API receives `TZ` from `TIME_ZONE` (`openshift/cirras-underwriting-api-deployment.yaml` lines 132-136; `.github/workflows/openshift-deploy.yml` line 123). The JVM therefore runs in `America/Vancouver`, which is the right choice, but applies the rules in the JRE's own time zone database, which predates 2026b in the pinned image.

The browser sends calendar dates as JavaScript `Date` values at local midnight, and the API stores them in PostgreSQL `date` columns through `java.util.Date` and the JVM zone. The database also writes audit timestamps with `now()` and `CURRENT_TIMESTAMP`, which use the session zone and PostgreSQL's own time zone data.

---

## 4. Areas of Concern

### U1. Calendar dates converted through a stale JRE: HIGH

- **User-entered dates.** The date pickers use `MomentDateAdapter` without `useUtc` (`app.module.ts` lines 131 and 385), so a picked date is local midnight. Forms such as `seeding-deadlines.component.ts` lines 80-83 and 167-170 build `new Date(...)` values, which serialize to JSON as UTC: 2026-12-01 becomes `2026-12-01T07:00:00Z`.
- **Columns affected.** MyBatis binds `seedingDate`, `seededDate`, `fullCoverageDeadlineDate`, `finalCoverageDeadlineDate` (and their defaults) and `declarationOfProductionDate` as `jdbcType=DATE`, and `bogMowedDate` and `bogRenovatedDate` as `jdbcType=TIMESTAMP` into `date` columns (for example `cuws.create.declared_yield_contract.sql` line 7 and `cuws.create.inventory_berries.sql` line 15). Every path converts the instant to a calendar day in the JVM zone.
- **CIRRAS-synced dates.** `effectiveDate` and `expiryDate` (32 `jdbcType=DATE` bindings) arrive from CIRRAS as epoch values, as in `nr-brmb-pit-claim`.
- **Dirty check.** `data/utils/DateUtils.java` line 79, `equalsDate`, compares values as `LocalDateTime` in `ZoneId.systemDefault()`. It is consistent within one JVM but reports a change whenever the sender and the API disagree.

On an unpatched JRE after 2026-11-01, `2026-12-01T07:00:00Z` is 2026-11-30 23:00 PST, so PostgreSQL stores `2026-11-30`.

### U2. Database audit timestamps: MEDIUM

60 mapper files set `CREATE_DATE` and `UPDATE_DATE` with `now()` or `CURRENT_TIMESTAMP` into `timestamp` (without time zone) columns. PostgreSQL converts `now()` to wall time in the session `TimeZone`, which pgJDBC sets from the JVM zone, using PostgreSQL's own tzdata from the Crunchy image. The API then reads the value back through the JVM zone. If the two carry different tzdata, the stored audit times are one hour off.

### U3. Reports: LOW

`services/reports/JasperReportServiceImpl.java` line 107 calls `JasperFillManager.fillReport(jasperReport, paramMap, dbConn)` without `REPORT_TIME_ZONE`, so JasperReports uses the JVM default zone. The reports format `java.util.Date` fields with `new SimpleDateFormat("MMM dd, yyyy")` and similar patterns (13 occurrences), and `CUWS_DOP_Berries.jrxml` line 36 and `CUWS_Inventory_Berries.jrxml` select `now() as date_printed` from the database. Printed dates follow U1 and U2: correct when both the JRE and the database are patched.

### U4. Sync ordering on wall-clock timestamps: LOW

`dataSyncTransDate` (43 `jdbcType=TIMESTAMP` bindings) is stored as JVM wall time without an offset and used to decide whether an incoming CIRRAS message is newer. During a mixed or late-patched period, an out-of-order message up to one hour older could overwrite newer data.

### U5. Outbox polling start time: LOW

`controllers/async/AsyncMasterTask.java` lines 90-108 schedule the first outbox fetch at a configured `LocalTime` in `ZoneId.systemDefault()`. An unpatched JRE starts it one hour off local time; the repeat interval is unaffected.

### U6. Active and expiry calculations: LOW

`CirrasDataSyncRsrcFactory.java` line 684 and `LandDataSyncRsrcFactory.java` line 290 (`calculateDates`) compare "now" with the expiry date as `LocalDateTime` in the JVM zone. Both sides use the same zone, so the result changes only within one hour of an expiry boundary.

### U7. Front end: LOW

- Plain `moment` 2.30.1, `ngx-moment` and the Angular `date` pipe follow the browser's own time zone data.
- `moment-timezone` 0.6.0 is installed (`package-lock.json`). It is stale (the BC rule arrived in 0.6.2), but its only references in `utils/index.ts` lines 133 and 137 are commented out.
- Crop-year defaults use `new Date().getFullYear()` and the inventory "is today" checks (`inventory-common.ts` lines 646 and 671) use browser local time. Both are unaffected.

### U8. Fail-over ownership: none

`cuws.process_failovr_ownership.expiry_timestamp` is the repository's only `timestamp with time zone` column. `SyncOwnershipMapper.xml` lines 20-21 and 33-34 compare `EXPIRY_TIMESTAMP AT TIME ZONE 'UTC'` with `now() AT TIME ZONE 'UTC'`, which is independent of any zone rules.

### U9. Backup schedule: none

`crunchy-postgres/charts/crunchy-postgres/values.yaml` lines 60-65 schedule backups in UTC; the comment says 08:00 UTC is midnight. After 2026-11-01 it is 01:00 local all year.

---

## 5. Areas of Failure

- **F1 (from U1), High likelihood if the JRE is not updated:** After 2026-11-01, a user enters a seeding date, coverage deadline or declaration of production date of 2026-12-01; the API stores 2026-11-30. Reports, deadline comparisons and CIRRAS synchronisation then use the wrong day. Dates synced from CIRRAS shift in the same way whenever CIRRAS and this API are not patched together.
- **F2 (from U2), Medium:** Audit times written by the database are one hour off if the PostgreSQL image and the JRE disagree.
- **F3 (from U4), Low:** An out-of-order sync message can overwrite newer data within a one-hour window during a mixed period.

---

## 6. Pre-existing Defects Found (independent of this change)

- **D1.** `AsyncMasterTask.java` line 94: `pollingTime.plusSeconds(getRandomSeconds());` discards its result because `LocalTime` is immutable, so the random stagger between nodes is never applied. The fix is `pollingTime = pollingTime.plusSeconds(getRandomSeconds());`. The same defect exists in `nr-brmb-pit-claim`.
- **D2.** `CirrasDataSyncRsrcFactory.java` lines 723-727 and `LandDataSyncRsrcFactory.java` lines 321-325: `getMaxExpiryDate()` calls `cal.set(9000, 12, 31)`. `Calendar` months are zero-based, so the result is 9001-01-31 at the current time of day. The same defect exists in `nr-brmb-pit-claim`.
- **D3.** `SyncOwnershipMapper.xml` returns `EXPIRY_TIMESTAMP AT TIME ZONE 'UTC'` as a timestamp without a zone, which pgJDBC reads as local time. `FailOverService.java` lines 59, 67, 76 and 89 therefore log the next expiry 7 hours later than the real time. Only the log message is affected; the expiry decision uses `EXPIRED_IND`, which is computed correctly.

---

## 7. Potential Resolutions

| # | Action | Owner | Priority |
|---|--------|-------|----------|
| R1 | Move both Dockerfiles to a Tomcat image on JRE 21.0.12 or later, confirm with `java -version`, and check `ZoneId.of("America/Vancouver").getRules().getOffset(Instant.parse("2026-12-01T12:00:00Z"))` returns `-07:00`. | Development and Platform | Before 2026-11-01 |
| R2 | Add `-Duser.timezone=America/Vancouver` to `CATALINA_OPTS` in `setenv.sh`, and confirm that `vars.TIME_ZONE` is `America/Vancouver` in every GitHub environment. | Development | Before 2026-11-01 |
| R3 | Confirm that the Crunchy PostgreSQL 17 image carries tzdata 2026b (for example `SELECT now() AT TIME ZONE 'America/Vancouver'` after 2026-11-01). | Platform | Before 2026-11-01 |
| R4 | Coordinate with the CIRRAS team so that CIRRAS and this API are patched in the same window. | Development | Before 2026-11-01 |
| R5 | Deploy all API pods from the same image so that no mixed period exists. | Platform | Before 2026-11-01 |
| R6 | Remove the unused `moment-timezone` dependency, or upgrade it to 0.6.5 or later if future code needs it. Longer term, send calendar dates as `YYYY-MM-DD` strings (for example `MomentDateAdapter` with `useUtc: true` and `LocalDate` in the API) to remove the JVM dependency. | Development | Optional |
| R7 | Update the backup schedule comment in `values.yaml`. | Development | Optional |
| R8 | Fix D1, D2 and D3. | Development | Independent |

---

## 8. Verification Checklist

1. In the API pod, run `java -version` and confirm 21.0.12 or later; confirm `TimeZone.getDefault().getID()` is `America/Vancouver`.
2. In a test environment with the clock set after 2026-11-01, enter a seeding date and a coverage deadline of 2026-12-01 in the UI, and confirm that the database stores `2026-12-01`.
3. Save an inventory record and confirm that `CREATE_DATE` and `UPDATE_DATE` match the wall-clock time.
4. Generate a DOP and an inventory report and confirm that the printed dates are correct.

---

## 9. Cross-References

- **nr-brmb-pit-claim** report: same platform, Dockerfile pattern, CIRRAS sync design and pre-existing defects D1 and D2.
- **nr-brmb-common** and **wfone-common-lib** reports: this API uses the `wfone-common` modules (`wfone-common-model`, `wfone-common-rest-endpoints`).
- **nr-brmb-pit-msg-queue** report: message transport between the PIT services.
