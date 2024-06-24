


-- Add is_hidden_on_printout_ind.
ALTER TABLE cuws.inventory_field ADD COLUMN is_hidden_on_printout_ind varchar(1);

COMMENT ON COLUMN cuws.inventory_field.is_hidden_on_printout_ind IS 'Is Hidden On Printout Ind determines if the planting is displayed on the printout (N) or not (Y)';


UPDATE inventory_field set is_hidden_on_printout_ind = 'N';

ALTER TABLE inventory_field ALTER COLUMN is_hidden_on_printout_ind SET NOT NULL;

-- Add underseeded_crop_variety_id.
ALTER TABLE cuws.inventory_field ADD COLUMN underseeded_crop_variety_id numeric(9, 0);

COMMENT ON COLUMN cuws.inventory_field.underseeded_crop_variety_id IS 'Underseeded Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id';

CREATE INDEX ix_if_cva ON cuws.inventory_field(underseeded_crop_variety_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.inventory_field ADD CONSTRAINT fk_if_cva 
    FOREIGN KEY (underseeded_crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;

-- Add underseeded_crop_variety_id.
ALTER TABLE cuws.inventory_field ADD COLUMN underseeded_acres numeric(10, 4);

COMMENT ON COLUMN cuws.inventory_field.underseeded_acres IS 'Underseeded Acres is the number of acres utilized by the underseeded variety. Entered by the user';


