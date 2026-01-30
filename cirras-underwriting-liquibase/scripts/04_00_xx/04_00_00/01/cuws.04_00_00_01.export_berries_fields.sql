\pset format csv
\echo "Exporting Existing Berries Fields"
\o data_export_import/BerriesFields.csv
\i sql/cuws.sql.export_berries_fields.sql
\o
\pset format aligned

