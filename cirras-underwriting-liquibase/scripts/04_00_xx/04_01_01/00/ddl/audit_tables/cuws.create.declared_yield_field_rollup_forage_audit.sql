CREATE TABLE cuws.declared_yield_field_rollup_forage_audit(
    declared_yield_field_rollup_forage_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                    varchar(10)       NOT NULL,
    audit_time_stamp                               timestamp(6)      NOT NULL,
    declared_yield_field_rollup_forage_guid        varchar(32)       NOT NULL,
    declared_yield_contract_guid                   varchar(32),
    commodity_type_code                            varchar(30),
    total_field_acres                              numeric(14, 4),
    total_bales_loads                              numeric(6, 0),
    harvested_acres                                numeric(14, 4),
    quantity_harvested_tons                        numeric(14, 4),
    yield_per_acre                                 numeric(14, 4),
    create_user                                    varchar(64)       NOT NULL,
    create_date                                    timestamp(0)      NOT NULL,
    update_user                                    varchar(64)       NOT NULL,
    update_date                                    timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.declared_yield_field_rollup_forage_audit_id IS 'Declared Yield Field Rollup Forage Audit Id is the ID of the Declared Yield Field Rollup Forage Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.declared_yield_field_rollup_forage_guid IS 'Declared Yield Field Rollup Forage Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.total_field_acres IS 'Total field Acres is the total quantity insured field acres from inventory seeded forage by comodity type'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.total_bales_loads IS 'Total Bales Loads is the number of bales or loads harvested on the field'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.harvested_acres IS 'Harvested Acres is the Harvested Acres for the Commodity from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.quantity_harvested_tons IS 'Quantity Harvested Tons is a calculated value from DOP and always in tons'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.yield_per_acre IS 'Yield Per Acre is a calculated value: Quantity Harvested / Harvested Acres. Taking the override values if they exist'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup_forage_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_rollup_forage_audit IS 'Declared Yield Field Rollup Forage Audit is the audit table for declared_yield_field_rollup_forage_audit'
;

ALTER TABLE cuws.declared_yield_field_rollup_forage_audit ADD 
    CONSTRAINT pk_dyfrfa PRIMARY KEY (declared_yield_field_rollup_forage_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_rollup_forage_audit ADD CONSTRAINT fk_dyfrfa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


