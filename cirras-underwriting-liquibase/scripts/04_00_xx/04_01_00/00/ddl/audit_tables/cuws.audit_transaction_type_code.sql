CREATE TABLE cuws.audit_transaction_type_code(
    audit_transaction_type_code    varchar(10)    NOT NULL,
    description                    varchar(10)    NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.audit_transaction_type_code.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.audit_transaction_type_code.description IS 'Descrition is the description for each audit transaction type code'
;
COMMENT ON TABLE cuws.audit_transaction_type_code IS 'The table contains the transaction type codes for the audit tables.'
;

ALTER TABLE cuws.audit_transaction_type_code ADD 
    CONSTRAINT pk_attc PRIMARY KEY (audit_transaction_type_code)
;

