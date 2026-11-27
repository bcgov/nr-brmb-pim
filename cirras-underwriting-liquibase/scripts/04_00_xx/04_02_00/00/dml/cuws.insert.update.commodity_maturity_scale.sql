/* 
   Update needs to be executed in a certain order to make sure a UK is not violated 
   (crop_commodity_id, plant_age, version_number)
*/
WITH ordered_records AS (
	SELECT commodity_maturity_scale_guid
	FROM commodity_maturity_scale
	ORDER BY plant_age DESC
)
update commodity_maturity_scale m set
plant_age = plant_age + 1,
update_user = 'CUWS_04_02_00', 
update_date = now()
FROM ordered_records o
WHERE m.commodity_maturity_scale_guid = o.commodity_maturity_scale_guid;

--Insert 0 plant age records for BLUEBERRY, RASPBERRY, CRANBERRY
INSERT INTO cuws.commodity_maturity_scale(commodity_maturity_scale_guid, crop_commodity_id, plant_age, scale, version_number, effective_crop_year, expiry_crop_year, create_user, create_date, update_user, update_date) SELECT replace(cast(gen_random_uuid() as text), '-', ''), (select crop_commodity_id from crop_commodity where commodity_name = 'BLUEBERRY'), 0, 0, 1, 2026, 9999, 'CUWS_04_02_00', now(),  'CUWS_04_02_00', now()WHERE NOT EXISTS (SELECT NULL FROM commodity_maturity_scale WHERE plant_age = 0 AND scale = 0 and crop_commodity_id = (select crop_commodity_id from crop_commodity where commodity_name = 'BLUEBERRY') );
INSERT INTO cuws.commodity_maturity_scale(commodity_maturity_scale_guid, crop_commodity_id, plant_age, scale, version_number, effective_crop_year, expiry_crop_year, create_user, create_date, update_user, update_date) SELECT replace(cast(gen_random_uuid() as text), '-', ''), (select crop_commodity_id from crop_commodity where commodity_name = 'RASPBERRY'), 0, 0, 1, 2026, 9999, 'CUWS_04_02_00', now(),  'CUWS_04_02_00', now()WHERE NOT EXISTS (SELECT NULL FROM commodity_maturity_scale WHERE plant_age = 0 AND scale = 0 and crop_commodity_id = (select crop_commodity_id from crop_commodity where commodity_name = 'RASPBERRY') );
INSERT INTO cuws.commodity_maturity_scale(commodity_maturity_scale_guid, crop_commodity_id, plant_age, scale, version_number, effective_crop_year, expiry_crop_year, create_user, create_date, update_user, update_date) SELECT replace(cast(gen_random_uuid() as text), '-', ''), (select crop_commodity_id from crop_commodity where commodity_name = 'CRANBERRY'), 0, 0, 1, 2026, 9999, 'CUWS_04_02_00', now(),  'CUWS_04_02_00', now()WHERE NOT EXISTS (SELECT NULL FROM commodity_maturity_scale WHERE plant_age = 0 AND scale = 0 and crop_commodity_id = (select crop_commodity_id from crop_commodity where commodity_name = 'CRANBERRY') );