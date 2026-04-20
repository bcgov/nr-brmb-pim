CREATE TABLE cuws.inventory_unseeded_audit(
    inventory_unseeded_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code    varchar(10)       NOT NULL,
    audit_time_stamp               timestamp(6)      NOT NULL,
    inventory_unseeded_guid        varchar(32)       NOT NULL,
    inventory_field_guid           varchar(32),
    crop_commodity_id              numeric(9, 0),
    crop_variety_id                numeric(9, 0),
    is_unseeded_insurable_ind      varchar(1),
    acres_to_be_seeded             numeric(10, 4),
    create_user                    varchar(64)       NOT NULL,
    create_date                    timestamp(0)      NOT NULL,
    update_user                    varchar(64)       NOT NULL,
    update_date                    timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_unseeded_audit.inventory_unseeded_audit_id IS 'Inventory Unseeded Audit Id is the ID of the Inventory Unseeded Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.inventory_unseeded_guid IS 'Inventory Unseeded GUID is the primary key used to identify a table record.'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.is_unseeded_insurable_ind IS 'Is Unseeded Insurable Ind determines if the specified field inventory is insurable for Unseeded Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_unseeded_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_unseeded_audit IS 'Inventory Unseeded Audit is the audit table for inventory_unseeded'
;

ALTER TABLE cuws.inventory_unseeded_audit ADD 
    CONSTRAINT pk_iua PRIMARY KEY (inventory_unseeded_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_unseeded_audit ADD CONSTRAINT fk_iua_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


