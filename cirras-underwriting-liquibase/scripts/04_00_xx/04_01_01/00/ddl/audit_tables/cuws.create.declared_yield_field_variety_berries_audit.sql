CREATE TABLE cuws.declared_yield_field_variety_berries_audit(
    declared_yield_field_variety_berries_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                      varchar(10)       NOT NULL,
    audit_time_stamp                                 timestamp(6)      NOT NULL,
    declared_yield_field_variety_berries_guid        varchar(32)       NOT NULL,
    declared_yield_field_commodity_berries_guid      varchar(32),
    crop_variety_id                                  numeric(9, 0),
    planted_acres                                    numeric(10, 4),
    mature_equivalent_acres                          numeric(10, 4),
    sold_shipped_yield                               numeric(14, 4),
    sales_yield                                      numeric(14, 4),
    abandonment_yield                                numeric(14, 4),
    total_production                                 numeric(14, 4),
    total_production_override                        numeric(14, 4),
    create_user                                      varchar(64)       NOT NULL,
    create_date                                      timestamp(0)      NOT NULL,
    update_user                                      varchar(64)       NOT NULL,
    update_date                                      timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.declared_yield_field_variety_berries_audit_id IS 'Declared Yield Field Variety Berries Audit Id is the ID of the Declared Yield Field Variety Berries  Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.declared_yield_field_variety_berries_guid IS 'Declared Yield Field Variety Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.declared_yield_field_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.planted_acres IS 'Planted Acres are the rolled up acres from inventory berries'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.mature_equivalent_acres IS 'Mature Equivalent Acres is the number of ME acres according to the planted year and scale'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.sold_shipped_yield IS 'Sold Shipped Yield is the total pounds of yield sold and shipped for the field and variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.sales_yield IS 'Sales Yield is the total pounds of yield private and direct sold for the field and variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.abandonment_yield IS 'Abandonment Yield is the total pounds of abandonment yield for the field and variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.total_production IS 'Total Production is the calculated total pounds of yield by variety and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.total_production_override IS 'Total Production Override is the manually entered total pounds of yield by variety and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_variety_berries_audit IS 'Declared Yield Field Variety Berries  Audit is the audit table for declared_yield_field_variety_berries'
;

ALTER TABLE cuws.declared_yield_field_variety_berries_audit ADD 
    CONSTRAINT pk_dyfvba PRIMARY KEY (declared_yield_field_variety_berries_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_variety_berries_audit ADD CONSTRAINT fk_dyfvba_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


