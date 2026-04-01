INSERT INTO cuws.audit_transaction_type_code(
	audit_transaction_type_code, 
	description,
	create_user, 
	create_date, 
	update_user, 
	update_date
) VALUES (
	'INSERT', 
	'Insert', 
	'CUWS_04_01_00', 
	now(), 
	'CUWS_04_01_00', 
	now()
);

INSERT INTO cuws.audit_transaction_type_code(
	audit_transaction_type_code, 
	description,
	create_user, 
	create_date, 
	update_user, 
	update_date
) VALUES (
	'UPDATE', 
	'Update',
	'CUWS_04_01_00', 
	now(), 
	'CUWS_04_01_00', 
	now()
);

INSERT INTO cuws.audit_transaction_type_code(
	audit_transaction_type_code,
	description,
	create_user, 
	create_date, 
	update_user, 
	update_date
) VALUES (
	'DELETE', 
	'Delete',
	'CUWS_04_01_00', 
	now(), 
	'CUWS_04_01_00', 
	now()
);