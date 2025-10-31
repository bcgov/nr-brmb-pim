INSERT INTO cuws.plant_insurability_type_code(
	plant_insurability_type_code,
	insurance_plan_id,
	description, 
	effective_date, 
	expiry_date, 
	create_user, 
	create_date, 
	update_user, 
	update_date
)
SELECT 'ST1',
       3,
       'Strawberry 1', 
       current_date, 
       '9999-12-31', 
       'CUWS_04_00_00', 
       now(), 
       'CUWS_04_00_00', 
       now()
WHERE NOT EXISTS (SELECT plant_insurability_type_code FROM plant_insurability_type_code WHERE plant_insurability_type_code = 'ST1');

INSERT INTO cuws.plant_insurability_type_code(
	plant_insurability_type_code,
	insurance_plan_id,
	description, 
	effective_date, 
	expiry_date, 
	create_user, 
	create_date, 
	update_user, 
	update_date
)
SELECT 'ST2',
       3,
       'Strawberry 2', 
       current_date, 
       '9999-12-31', 
       'CUWS_04_00_00', 
       now(), 
       'CUWS_04_00_00', 
       now()
WHERE NOT EXISTS (SELECT plant_insurability_type_code FROM plant_insurability_type_code WHERE plant_insurability_type_code = 'ST2');
