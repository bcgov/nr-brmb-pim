CREATE TABLE cuws.inventory_field_audit(
    inventory_field_audit_id                    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                 varchar(10)       NOT NULL,
    audit_time_stamp                            timestamp(6)      NOT NULL,
    inventory_field_guid                        varchar(32)       NOT NULL,
    insurance_plan_id                           numeric(9, 0),
    field_id                                    numeric(9, 0),
    last_year_crop_commodity_id                 numeric(9, 0),
    last_year_crop_variety_id                   numeric(9, 0),
    underseeded_crop_variety_id                 numeric(9, 0),
    underseeded_inventory_seeded_forage_guid    varchar(32),
    crop_year                                   numeric(4, 0),
    planting_number                             numeric(2, 0),
    is_hidden_on_printout_ind                   varchar(1),
    underseeded_acres                           numeric(10, 4),
    create_user                                 varchar(64)       NOT NULL,
    create_date                                 timestamp(0)      NOT NULL,
    update_user                                 varchar(64)       NOT NULL,
    update_date                                 timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_field_audit.inventory_field_audit_id IS 'Inventory Field Audit Id is the ID of the Inventory Field Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_field_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_field_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_field_audit.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_field_audit.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.inventory_field_audit.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.inventory_field_audit.last_year_crop_commodity_id IS 'Last Years Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_field_audit.last_year_crop_variety_id IS 'Last Year Crop Variety Id is a unique Id of a variety from crop_variety'
;
COMMENT ON COLUMN cuws.inventory_field_audit.underseeded_crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_field_audit.underseeded_inventory_seeded_forage_guid IS 'Inventory Seeded Forage GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_field_audit.crop_year IS 'Crop Year is the year for which the crop is insured'
;
COMMENT ON COLUMN cuws.inventory_field_audit.planting_number IS 'Planting Number is the order in which the plantings were added to the field.'
;
COMMENT ON COLUMN cuws.inventory_field_audit.is_hidden_on_printout_ind IS 'Is Hidden On Printout Ind determines if the planting is displayed on the printout (N) or not (Y)'
;
COMMENT ON COLUMN cuws.inventory_field_audit.underseeded_acres IS 'Underseeded Acres is the number of acres utilized by the underseeded variety. Entered by the user'
;
COMMENT ON COLUMN cuws.inventory_field_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_field_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_field_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_field_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_field_audit IS 'Inventory Field Audit is the audit table for inventory_field'
;

ALTER TABLE cuws.inventory_field_audit ADD 
    CONSTRAINT pk_ifa PRIMARY KEY (inventory_field_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_field_audit ADD CONSTRAINT fk_ifa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


