CREATE TABLE cuws.declared_yield_field_rollup_audit(
    declared_yield_field_rollup_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code             varchar(10)       NOT NULL,
    audit_time_stamp                        timestamp(6)      NOT NULL,
    declared_yield_field_rollup_guid        varchar(32)       NOT NULL,
    declared_yield_contract_guid            varchar(32),
    crop_commodity_id                       numeric(9, 0),
    is_pedigree_ind                         varchar(1),
    estimated_yield_per_acre_tonnes         numeric(14, 4),
    estimated_yield_per_acre_bushels        numeric(14, 4),
    create_user                             varchar(64)       NOT NULL,
    create_date                             timestamp(0)      NOT NULL,
    update_user                             varchar(64)       NOT NULL,
    update_date                             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.declared_yield_field_rollup_audit_id IS 'Declared Yield Field Rollup Audit Id is the ID of the Declared Yield Field Rollup Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.declared_yield_field_rollup_guid IS 'Declared Yield Field Rollup Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.declared_yield_contract_guid IS 'Declared Yield Contract Guid links to a record in DECLARED_YIELD_CONTRACT table'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.estimated_yield_per_acre_tonnes IS 'Estimated Yield Per Acre Tonnes is the declared estimated amount of yield produced for 1 acre, in Tonnes. It is rolled-up from DECLARED_YIELD_FIELD.ESTIMATED_YIELD_PER_ACRE and converted if necessary.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.estimated_yield_per_acre_bushels IS 'Estimated Yield Per Acre Bushels is the declared estimated amount of yield produced for 1 acre, in Bushels. It is rolled-up from DECLARED_YIELD_FIELD.ESTIMATED_YIELD_PER_ACRE and converted if necessary.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_rollup_audit IS 'Declared Yield Field Rollup Audit is the audit table for declared_yield_field_rollup_audit'
;

ALTER TABLE cuws.declared_yield_field_rollup_audit ADD 
    CONSTRAINT pk_dyfra PRIMARY KEY (declared_yield_field_rollup_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_rollup_audit ADD CONSTRAINT fk_dyfra_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


