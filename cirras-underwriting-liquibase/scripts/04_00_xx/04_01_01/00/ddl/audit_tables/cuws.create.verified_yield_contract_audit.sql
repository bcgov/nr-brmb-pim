CREATE TABLE cuws.verified_yield_contract_audit(
    verified_yield_contract_audit_id     numeric(9, 0)    NOT NULL,
    audit_transaction_type_code          varchar(10)      NOT NULL,
    audit_time_stamp                     timestamp(6)     NOT NULL,
    verified_yield_contract_guid         varchar(32)      NOT NULL,
    contract_id                          numeric(9, 0),
    crop_year                            numeric(4, 0),
    declared_yield_contract_guid         varchar(32),
    default_yield_meas_unit_type_code    varchar(10),
    verified_yield_update_timestamp      timestamp(0),
    verified_yield_update_user           varchar(64),
    create_user                          varchar(64)      NOT NULL,
    create_date                          timestamp(0)     NOT NULL,
    update_user                          varchar(64)      NOT NULL,
    update_date                          timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_contract_audit.verified_yield_contract_audit_id IS 'Verified Yield Contract Audit Id is the ID of the Verified Yield Contract Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id (also in POLICY.CONTRACT_ID)'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.crop_year IS 'Crop Year is the year the grower is bound by the specified contract from cirr_grower_contract_years.crop_year'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.default_yield_meas_unit_type_code IS 'Yield Meas Unit Type Code is a unique record identifier for yield meas unit type records.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.verified_yield_update_timestamp IS 'Verified Yield Update Timestamp is the last time any DOP data was changed by the user.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.verified_yield_update_user IS 'Verified Yield Update User is the last user that changed any Verified Yield data.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.verified_yield_contract_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_contract_audit IS 'Verified Yield Contract Audit is the audit table for verified_yield_contract.'
;

ALTER TABLE cuws.verified_yield_contract_audit ADD 
    CONSTRAINT pk_vyca PRIMARY KEY (verified_yield_contract_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_contract_audit ADD CONSTRAINT fk_vyca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


