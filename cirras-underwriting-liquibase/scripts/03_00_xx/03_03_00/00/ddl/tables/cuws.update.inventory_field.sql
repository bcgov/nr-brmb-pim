-- Add new column
ALTER TABLE cuws.inventory_field ADD COLUMN last_year_crop_variety_id numeric(9, 0);

COMMENT ON COLUMN cuws.inventory_field.last_year_crop_variety_id IS 'Last Year Crop Variety Id is a unique Id of a variety from crop_variety'
;

CREATE INDEX ix_if_cva2 ON cuws.inventory_field(last_year_crop_variety_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.inventory_field ADD CONSTRAINT fk_if_cva2 
    FOREIGN KEY (last_year_crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;