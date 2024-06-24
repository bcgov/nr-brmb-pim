CREATE TABLE cuws.inventory_seeded_grain(
    inventory_seeded_grain_guid    varchar(32)       NOT NULL,
    inventory_field_guid           varchar(32)       NOT NULL,
    crop_commodity_id              numeric(9, 0)     NOT NULL,
    crop_variety_id                numeric(9, 0)     NOT NULL,
    commodity_type_code            varchar(30)       NOT NULL,
    is_quantity_insurable_ind      varchar(1)        NOT NULL,
    is_replaced_ind                varchar(1)        NOT NULL,
    is_pedigree_ind                varchar(1)        NOT NULL,
    seeding_date                   date,
    seeded_acres                   numeric(10, 4)    NOT NULL,
    create_user                    varchar(64)       NOT NULL,
    create_date                    timestamp(0)      NOT NULL,
    update_user                    varchar(64)       NOT NULL,
    update_date                    timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_seeded_grain.inventory_seeded_grain_guid IS 'Inventory Seeded Grain GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.is_quantity_insurable_ind IS 'Is Quantity Insurable Ind determines if the specified field inventory is insurable for Quantity Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.is_replaced_ind IS 'Is Replaced Ind is Y if this planting has been replaced by a newer one, otherwise N. '
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.is_pedigree_ind IS 'Is Pedigree Ind determines if the crop was pedigree.'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.seeding_date IS 'Seeding Date is the date on which the commodity was seeded'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.seeded_acres IS 'Seeded Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_seeded_grain.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_seeded_grain IS 'The table contains seeded grain inventory data. Reseeded plantings join to the same INVENTORY_FIELD record'
;

CREATE INDEX ix_isg_if ON cuws.inventory_seeded_grain(inventory_field_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_isg_cco ON cuws.inventory_seeded_grain(crop_commodity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_isg_cva ON cuws.inventory_seeded_grain(crop_variety_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_isg_ctc ON cuws.inventory_seeded_grain(commodity_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_seeded_grain ADD 
    CONSTRAINT pk_isg PRIMARY KEY (inventory_seeded_grain_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_seeded_grain ADD CONSTRAINT fk_isg_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;

ALTER TABLE cuws.inventory_seeded_grain ADD CONSTRAINT fk_isg_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.inventory_seeded_grain ADD CONSTRAINT fk_isg_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;

ALTER TABLE cuws.inventory_seeded_grain ADD CONSTRAINT fk_isg_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES cuws.commodity_type_code(commodity_type_code)
;


