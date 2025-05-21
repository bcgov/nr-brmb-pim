ALTER TABLE cuws.verified_yield_amendment ADD COLUMN crop_variety_id numeric(9, 0);

COMMENT ON COLUMN cuws.verified_yield_amendment.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety'
;

CREATE INDEX ix_vya_cva ON cuws.verified_yield_amendment(crop_variety_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.verified_yield_amendment ADD CONSTRAINT fk_vya_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;
