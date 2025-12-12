\o cuws.04_00_00_01.prepare_berries_inv.dml.PROD.log


-- Load plantings for 2026 BERRIES policies.
\i dml/cuws.dml.insert_berries_2026_staging.sql

-- Transform and validate.
\i dml/cuws.dml.transform_berries_2026_staging.sql
\i dml/cuws.dml.validate_berries_2026_staging.sql

\o
