CREATE TABLE cuws.inventory_berries(
    inventory_berries_guid       varchar(32)       NOT NULL,
    inventory_field_guid         varchar(32)       NOT NULL,
    crop_commodity_id            numeric(9, 0)     NOT NULL,
    crop_variety_id              numeric(9, 0),
    planted_year                 numeric(4, 0),
    planted_acres                numeric(10, 4),
    row_spacing                  numeric(4, 0),
    plant_spacing                numeric(10, 4),
    total_plants                 numeric(10, 0),
    is_quantity_insurable_ind    varchar(1)        NOT NULL,
    is_plant_insurable_ind       varchar(1)        NOT NULL,
    create_user                  varchar(64)       NOT NULL,
    create_date                  timestamp(0)      NOT NULL,
    update_user                  varchar(64)       NOT NULL,
    update_date                  timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_berries.inventory_berries_guid IS 'Inventory Berries Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_berries.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_berries.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_berries.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_berries.planted_year IS 'Planted Year was the year when the variety was planted'
;
COMMENT ON COLUMN cuws.inventory_berries.planted_acres IS 'Planted Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_berries.row_spacing IS 'Row spacing is a measure of distance between rows.'
;
COMMENT ON COLUMN cuws.inventory_berries.plant_spacing IS 'Plant spacing is a measure of distance between rows.'
;
COMMENT ON COLUMN cuws.inventory_berries.total_plants IS 'Total plants is a count of the plants of the specified variety planted on the field.'
;
COMMENT ON COLUMN cuws.inventory_berries.is_quantity_insurable_ind IS 'Is Quantity Insured flag determines if the specified crop is quantity insurable (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_berries.is_plant_insurable_ind IS 'Is Plant Insured flag determines if the specified crop is plant insurable (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_berries.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_berries.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_berries.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_berries.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_berries IS 'The table contains inventory for berries by field and crop year.'
;

CREATE INDEX ix_ib_if ON cuws.inventory_berries(inventory_field_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_ib_cva ON cuws.inventory_berries(crop_variety_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_ib_cco ON cuws.inventory_berries(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_berries ADD 
    CONSTRAINT pk_ib PRIMARY KEY (inventory_berries_guid)
;

ALTER TABLE cuws.inventory_berries ADD 
    CONSTRAINT uk_ib UNIQUE (inventory_field_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_berries ADD CONSTRAINT fk_ib_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;

ALTER TABLE cuws.inventory_berries ADD CONSTRAINT fk_ib_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;

ALTER TABLE cuws.inventory_berries ADD CONSTRAINT fk_ib_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


