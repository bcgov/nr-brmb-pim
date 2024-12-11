\o cirras.cuws.03_06_00_01.import_cirras_data.dml.log

\qecho Import product
\copy cuws.product(product_id, policy_id, crop_commodity_id, commodity_coverage_code, product_status_code, deductible_level, production_guarantee, probable_yield, insured_by_meas_type, insurable_value_hundred_percent, coverage_dollars, data_sync_trans_date, create_user, create_date, update_user, update_date) FROM data_export_import/product.csv WITH DELIMITER '|' NULL AS 'NULL'

\o
