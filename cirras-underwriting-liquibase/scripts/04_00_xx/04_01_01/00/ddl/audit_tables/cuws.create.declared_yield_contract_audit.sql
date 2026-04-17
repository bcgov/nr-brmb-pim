CREATE TABLE cuws.declared_yield_contract_audit(
    declared_yield_contract_audit_id     numeric(9, 0)    NOT NULL,
    audit_transaction_type_code          varchar(10)      NOT NULL,
    audit_time_stamp                     timestamp(6)     NOT NULL,
    declared_yield_contract_guid         varchar(32)      NOT NULL,
    contract_id                          numeric(9, 0),
    crop_year                            numeric(4, 0),
    declaration_of_production_date       date,
    dop_update_timestamp                 timestamp(0),
    dop_update_user                      varchar(64),
    entered_yield_meas_unit_type_code    varchar(10),
    default_yield_meas_unit_type_code    varchar(10),
    grain_from_other_source_ind          varchar(1),
    baler_wagon_info                     varchar(128),
    total_livestock                      numeric(5, 0),
    create_user                          varchar(64)      NOT NULL,
    create_date                          timestamp(0)     NOT NULL,
    update_user                          varchar(64)      NOT NULL,
    update_date                          timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_contract_audit.declared_yield_contract_audit_id IS 'Declared Yield Contract Audit Id is the ID of the Declared Yield Contract Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.declaration_of_production_date IS 'Declaration Of Production Date is the date the Declaration of Production was received.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.dop_update_timestamp IS 'Dop Update Timestamp is the last time any DOP data was changed by the user.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.dop_update_user IS 'Dop Update User is the last user that changed any DOP data.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.entered_yield_meas_unit_type_code IS 'Entered Yield Meas Unit Type Code is the unit in which declared yield has been entered (Bushels or Tonnes).'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.default_yield_meas_unit_type_code IS 'Default Yield Meas Unit Type Code is the default unit for the plan (Bushels or Tonnes).'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.grain_from_other_source_ind IS 'Grain From Other Source Ind indicates if any grain has been acquired from a source other than inventory.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.baler_wagon_info IS 'Baler Wagon Info is the make and model of a baler or wagon'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.total_livestock IS 'Total Livestock is the number of livestock'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_contract_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_contract_audit IS 'Declared Yield Contract Audit is the audit table for declared_yield_contract'
;

ALTER TABLE cuws.declared_yield_contract_audit ADD 
    CONSTRAINT pk_dyca PRIMARY KEY (declared_yield_contract_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_audit ADD CONSTRAINT fk_dyca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


