CREATE TABLE cuws.inventory_seeded_grain_audit(
    inventory_seeded_grain_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code        varchar(10)       NOT NULL,
    audit_time_stamp                   timestamp(6)      NOT NULL,
    inventory_seeded_grain_guid        varchar(32)       NOT NULL,
    inventory_field_guid               varchar(32),
    crop_commodity_id                  numeric(9, 0),
    crop_variety_id                    numeric(9, 0),
    commodity_type_code                varchar(30),
    is_quantity_insurable_ind          varchar(1),
    is_spot_loss_insurable_ind         varchar(1),
    is_replaced_ind                    varchar(1),
    is_pedigree_ind                    varchar(1),
    seeding_date                       date,
    seeded_acres                       numeric(10, 4),
    create_user                        varchar(64)       NOT NULL,
    create_date                        timestamp(0)      NOT NULL,
    update_user                        varchar(64)       NOT NULL,
    update_date                        timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.inventory_seeded_grain_audit_id IS 'Inventory Seeded Grain Audit Id is the ID of the Inventory Seeded Grain Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.inventory_seeded_grain_guid IS 'Inventory Seeded Grain GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.is_quantity_insurable_ind IS 'Is Quantity Insurable Ind determines if the specified field inventory is insurable for Quantity Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.is_spot_loss_insurable_ind IS 'Is Spot Loss Insurable Ind determines if the specified planting is insurable for Grain Spot Loss in CIRRAS (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.is_replaced_ind IS 'Is Replaced Ind is Y if this planting has been replaced by a newer one, otherwise N. '
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the crop was pedigree.'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.seeding_date IS 'Seeding Date is the date on which the commodity was seeded'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.seeded_acres IS 'Seeded Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_seeded_grain_audit IS 'Inventory Seeded Grain Audit is the audit table for inventory_seeded_grain'
;

ALTER TABLE cuws.inventory_seeded_grain_audit ADD 
    CONSTRAINT pk_isga PRIMARY KEY (inventory_seeded_grain_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_seeded_grain_audit ADD CONSTRAINT fk_isga_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


