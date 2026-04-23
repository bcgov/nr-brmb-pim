CREATE TABLE cuws.declared_yield_field_audit(
    declared_yield_field_audit_id            numeric(9, 0)     NOT NULL,
    audit_transaction_type_code              varchar(10)       NOT NULL,
    audit_time_stamp                         timestamp(6)      NOT NULL,
    declared_yield_field_guid                varchar(32)       NOT NULL,
    inventory_field_guid                     varchar(32),
    estimated_yield_per_acre                 numeric(14, 4),
    estimated_yield_per_acre_default_unit    numeric(14, 4),
    unharvested_acres_ind                    varchar(1),
    create_user                              varchar(64)       NOT NULL,
    create_date                              timestamp(0)      NOT NULL,
    update_user                              varchar(64)       NOT NULL,
    update_date                              timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_audit.declared_yield_field_audit_id IS 'Declared Yield Field Audit Id is the ID of the Declared Yield Field Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.declared_yield_field_guid IS 'Declared Yield Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.estimated_yield_per_acre IS 'Estimated Yield Per Acre is the declared estimated amount of yield produced for 1 acre, in DECLARED_YIELD_CONTRACT.ENTERED_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.estimated_yield_per_acre_default_unit IS 'Estimated Yield Per Acre Default Unit is the declared estimated amount of yield produced for 1 acre, in DECLARED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.unharvested_acres_ind IS 'Unharvested Acres Ind is Y if there are acres of a planting that the grower declares that were not harvested.'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_audit IS 'Declared Yield Field Audit is the audit table for declared_yield_field'
;

ALTER TABLE cuws.declared_yield_field_audit ADD 
    CONSTRAINT pk_dyfa PRIMARY KEY (declared_yield_field_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_audit ADD CONSTRAINT fk_dyfa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


