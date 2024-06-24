CREATE TABLE cuws.inventory_unseeded(
    inventory_unseeded_guid      varchar(32)       NOT NULL,
    inventory_field_guid         varchar(32)       NOT NULL,
    crop_commodity_id            numeric(9, 0),
    is_unseeded_insurable_ind    varchar(1)        NOT NULL,
    acres_to_be_seeded           numeric(10, 4),
    create_user                  varchar(64)       NOT NULL,
    create_date                  timestamp(0)      NOT NULL,
    update_user                  varchar(64)       NOT NULL,
    update_date                  timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_unseeded.inventory_unseeded_guid IS 'Inventory Unseeded GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_unseeded.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.inventory_unseeded.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_unseeded.is_unseeded_insurable_ind IS 'Is Unseeded Insurable Ind determines if the specified field inventory is insurable for Unseeded Coverage in CIRRAS (Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_unseeded.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_unseeded.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_unseeded.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_unseeded.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_unseeded IS 'The table contains inventory seeding intentions data. It''s in a 1:1 relationship with INVENTORY_FIELD'
;

CREATE INDEX ix_iu_cco ON cuws.inventory_unseeded(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_unseeded ADD 
    CONSTRAINT pk_iu PRIMARY KEY (inventory_unseeded_guid) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cuws.inventory_unseeded ADD 
    CONSTRAINT uk_iu UNIQUE (inventory_field_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_unseeded ADD CONSTRAINT fk_iu_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;

ALTER TABLE cuws.inventory_unseeded ADD CONSTRAINT fk_iu_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


