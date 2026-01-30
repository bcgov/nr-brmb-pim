\o cuws.04_00_00_01.prepare_berries_inv.dml.PROD.one.log


-- Load plantings for 2026 BERRIES policies.
\i dml/cuws.dml.insert_berries_2026_staging.sql

-- Run pre-validation extracts.
-- These will generate csv files in the sql folder.
\o
\cd sql
\i cuws.sql.validate_all.sql
\cd ..

-- Set output back to a log file.
\o cuws.04_00_00_01.prepare_berries_inv.dml.PROD.two.log

-- Transform and validate.
\i dml/cuws.dml.transform_berries_2026_staging.sql
\i dml/cuws.dml.validate_berries_2026_staging.sql

\o
