ALTER TABLE cuws.inventory_unseeded ADD COLUMN crop_variety_id numeric(9, 0);

COMMENT ON COLUMN cuws.inventory_unseeded.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;

CREATE INDEX ix_iu_cva ON cuws.inventory_unseeded(crop_variety_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.inventory_unseeded ADD CONSTRAINT fk_iu_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;