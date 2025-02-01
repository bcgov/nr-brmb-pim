INSERT INTO cuws.underwriting_comment_type_code(
	underwriting_comment_type_code, 
	description, 
	effective_date, 
	expiry_date, 
	create_user, 
	create_date, 
	update_user, 
	update_date
)
SELECT 	'VY', 
		'Verified Yield', 
		current_date, 
		'9999-12-31', 
		'CUWS_01_00_00', 
		now(), 
		'CUWS_01_00_00', 
		now()
WHERE NOT EXISTS (SELECT underwriting_comment_type_code FROM underwriting_comment_type_code WHERE underwriting_comment_type_code = 'VY');
