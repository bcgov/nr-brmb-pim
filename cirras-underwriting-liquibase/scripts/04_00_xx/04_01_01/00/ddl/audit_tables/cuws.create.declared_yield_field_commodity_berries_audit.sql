CREATE TABLE cuws.declared_yield_field_commodity_berries_audit(
    declared_yield_field_commodity_berries_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                        varchar(10)       NOT NULL,
    audit_time_stamp                                   timestamp(6)      NOT NULL,
    declared_yield_field_commodity_berries_guid        varchar(32)       NOT NULL,
    field_id                                           numeric(9, 0),
    crop_commodity_id                                  numeric(9, 0),
    crop_year                                          numeric(4, 0),
    total_production                                   numeric(14, 4),
    total_production_override                          numeric(14, 4),
    total_planted_acres                                numeric(10, 4),
    total_mature_equivalent_acres                      numeric(10, 4),
    create_user                                        varchar(64)       NOT NULL,
    create_date                                        timestamp(0)      NOT NULL,
    update_user                                        varchar(64)       NOT NULL,
    update_date                                        timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.declared_yield_field_commodity_berries_audit_id IS 'Declared Yield Field Commodity Berries Audit Id is the ID of the Declared Yield Field Commodity Berries Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.declared_yield_field_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.total_production IS 'Total Production is the calculated total pounds of yield by commdity and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.total_production_override IS 'Total Production Override is the manually entered total pounds of yield by commodity and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.total_planted_acres IS 'Total Planted Acres are the calculated total planted acres from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.total_mature_equivalent_acres IS 'Total Mature Equivalent Acres are the calculated total ME acres from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_commodity_berries_audit IS 'Declared Yield Field Commodity Audit is the audit table for declared_yield_field_commodity_berries'
;

ALTER TABLE cuws.declared_yield_field_commodity_berries_audit ADD 
    CONSTRAINT pk_dyfcba PRIMARY KEY (declared_yield_field_commodity_berries_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_commodity_berries_audit ADD CONSTRAINT fk_dyfcba_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


