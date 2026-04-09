CREATE TABLE cuws.audit_transaction_type_code(
    audit_transaction_type_code    varchar(10)    NOT NULL,
    description                    varchar(10)    NOT NULL,
    create_user                    varchar(64)    NOT NULL,
    create_date                    timestamp(0)   NOT NULL,
    update_user                    varchar(64)    NOT NULL,
    update_date                    timestamp(0)   NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.audit_transaction_type_code.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.audit_transaction_type_code.description IS 'Descrition is the description for each audit transaction type code'
;
COMMENT ON COLUMN cuws.audit_transaction_type_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.audit_transaction_type_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.audit_transaction_type_code.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN cuws.audit_transaction_type_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.audit_transaction_type_code IS 'The table contains the transaction type codes for the audit tables.'
;

ALTER TABLE cuws.audit_transaction_type_code ADD 
    CONSTRAINT pk_attc PRIMARY KEY (audit_transaction_type_code) USING INDEX TABLESPACE pg_default 
;