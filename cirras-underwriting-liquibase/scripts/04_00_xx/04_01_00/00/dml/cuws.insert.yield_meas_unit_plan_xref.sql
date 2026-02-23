--Insert yield measurement unit for berries
INSERT INTO cuws.yield_meas_unit_plan_xref(
	yield_meas_unit_plan_xref_guid, 
	yield_meas_unit_type_code, 
	insurance_plan_id, 
	is_default_yield_unit_ind, 
	create_user, 
	create_date, 
	update_user, 
	update_date)
	SELECT
		replace(cast(gen_random_uuid() as text), '-', ''),
		'LB',
		3,
		'Y',
		'CUWS_04_01_00', 
		now(), 
		'CUWS_04_01_00', 
		now()
		WHERE NOT EXISTS 
		(SELECT NULL 
		 FROM yield_meas_unit_plan_xref
		 WHERE yield_meas_unit_type_code = 'LB' AND insurance_plan_id = 3
		);