CREATE TABLE cuws.inventory_seeded_forage(
    inventory_seeded_forage_guid    varchar(32)       NOT NULL,
    inventory_field_guid            varchar(32)       NOT NULL,
    crop_commodity_id               numeric(9, 0),
    crop_variety_id                 numeric(9, 0),
    commodity_type_code             varchar(30),
    field_acres                     numeric(10, 4),
    seeding_year                    numeric(4, 0),
	seeding_date                    date,
    is_irrigated_ind                varchar(1)        NOT NULL,
    is_quantity_insurable_ind       varchar(1)        NOT NULL,
    plant_insurability_type_code    varchar(10),
    is_awp_eligible_ind             varchar(1)        NOT NULL,
    create_user                     varchar(64)       NOT NULL,
    create_date                     timestamp(0)      NOT NULL,
    update_user                     varchar(64)       NOT NULL,
    update_date                     timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_seeded_forage.inventory_seeded_forage_guid IS 'Inventory Seeded Forage GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.field_acres IS 'Field Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.seeding_year IS 'Seeding Year is the year on which the commodity was seeded'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.seeding_date IS 'Seeding Date is the date on which the commodity was seeded'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.is_irrigated_ind IS 'Is Irrigated Ind determines if the specified planting is irrigated (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.is_quantity_insurable_ind IS 'Is Quantity Insurable Ind determines if the specified field inventory is insurable for Quantity Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.plant_insurability_type_code IS 'Plant Insurability Type Code is a unique record identifier for plant insurability type records.'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.is_awp_eligible_ind IS 'Is AWP Eligible Ind determines if the planting is eligible for AWP'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_seeded_forage.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_seeded_forage IS 'The table contains seeded forage inventory data.'
;

CREATE INDEX ix_isf_if ON cuws.inventory_seeded_forage(inventory_field_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_isf_cco ON cuws.inventory_seeded_forage(crop_commodity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_isf_cva ON cuws.inventory_seeded_forage(crop_variety_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_isf_ctc ON cuws.inventory_seeded_forage(commodity_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_isf_pitc ON cuws.inventory_seeded_forage(plant_insurability_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_seeded_forage ADD 
    CONSTRAINT pk_isf PRIMARY KEY (inventory_seeded_forage_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_seeded_forage ADD CONSTRAINT fk_isf_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;

ALTER TABLE cuws.inventory_seeded_forage ADD CONSTRAINT fk_isf_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.inventory_seeded_forage ADD CONSTRAINT fk_isf_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;

ALTER TABLE cuws.inventory_seeded_forage ADD CONSTRAINT fk_isf_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES cuws.commodity_type_code(commodity_type_code)
;

ALTER TABLE cuws.inventory_seeded_forage ADD CONSTRAINT fk_pitc_isf 
    FOREIGN KEY (plant_insurability_type_code)
    REFERENCES cuws.plant_insurability_type_code(plant_insurability_type_code)
;


