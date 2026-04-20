CREATE TABLE cuws.inventory_seeded_forage_audit(
    inventory_seeded_forage_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code         varchar(10)       NOT NULL,
    audit_time_stamp                    timestamp(6)      NOT NULL,
    inventory_seeded_forage_guid        varchar(32)       NOT NULL,
    inventory_field_guid                varchar(32),
    crop_commodity_id                   numeric(9, 0),
    crop_variety_id                     numeric(9, 0),
    commodity_type_code                 varchar(30),
    field_acres                         numeric(10, 4),
    seeding_year                        numeric(4, 0),
    seeding_date                        date,
    is_irrigated_ind                    varchar(1),
    is_quantity_insurable_ind           varchar(1),
    plant_insurability_type_code        varchar(10),
    is_awp_eligible_ind                 varchar(1),
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(0)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.inventory_seeded_forage_audit_id IS 'Inventory Seeded Forage Audit Id is the ID of the Inventory Seeded Forage Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.inventory_seeded_forage_guid IS 'Inventory Seeded Forage GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.field_acres IS 'Field Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.seeding_year IS 'Seeding Year is the year on which the commodity was seeded'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.seeding_date IS 'Seeding Date is the date on which the commodity was seeded'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.is_irrigated_ind IS 'Is Irrigated Ind determines if the specified planting is irrigated (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.is_quantity_insurable_ind IS 'Is Quantity Insurable Ind determines if the specified field inventory is insurable for Quantity Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.plant_insurability_type_code IS 'Plant Insurability Type Code is a unique record identifier for plant insurability type records.'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.is_awp_eligible_ind IS 'Is AWP Eligible Ind determines if the planting is eligible for AWP'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_seeded_forage_audit IS 'Inventory Seeded Forage Audit is the audit table for inventory_seeded_forage'
;

ALTER TABLE cuws.inventory_seeded_forage_audit ADD 
    CONSTRAINT pk_isfa PRIMARY KEY (inventory_seeded_forage_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_seeded_forage_audit ADD CONSTRAINT fk_isfa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


