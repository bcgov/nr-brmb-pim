--Insert verified yield amendment codes

INSERT INTO cuws.verified_yield_amendment_code(verified_yield_amendment_code, description, effective_date, expiry_date, create_user, create_date, update_user, update_date)
SELECT 	'Appraisal', 
		'Appraisal', 
		current_date, 
		'9999-12-31', 
		'CUWS_03_06_00', 
		now(), 
		'CUWS_03_06_00', 
		now()
WHERE NOT EXISTS (SELECT verified_yield_amendment_code FROM verified_yield_amendment_code WHERE verified_yield_amendment_code = 'Appraisal');

INSERT INTO cuws.verified_yield_amendment_code(verified_yield_amendment_code, description, effective_date, expiry_date, create_user, create_date, update_user, update_date)
SELECT 	'Assessment', 
		'Assessment', 
		current_date, 
		'9999-12-31', 
		'CUWS_03_06_00', 
		now(), 
		'CUWS_03_06_00', 
		now()
WHERE NOT EXISTS (SELECT verified_yield_amendment_code FROM verified_yield_amendment_code WHERE verified_yield_amendment_code = 'Assessment');
