

--Add new COLUMNS
ALTER TABLE cuws.crop_commodity ADD COLUMN is_product_insurable_ind varchar(1);
ALTER TABLE cuws.crop_commodity ADD COLUMN is_crop_insurance_eligible_ind varchar(1);
ALTER TABLE cuws.crop_commodity ADD COLUMN is_plant_insurance_eligible_ind varchar(1);
ALTER TABLE cuws.crop_commodity ADD COLUMN is_other_insurance_eligible_ind varchar(1);

COMMENT ON COLUMN cuws.crop_commodity.is_product_insurable_ind IS 'Is Product Insurable Ind indicates whether the CROP TYPE can be insured (Y/N) from cirr_crop_types.product_insurable_flag'
;
COMMENT ON COLUMN cuws.crop_commodity.is_crop_insurance_eligible_ind IS 'Is Crop Insurance Eligible indicates if the crop type is eligible to for crop yield insurance (Y) or not (N) from cirr_crop_types.is_crop_insrnce_eligible_flag'
;
COMMENT ON COLUMN cuws.crop_commodity.is_plant_insurance_eligible_ind IS 'Is Plant Insurance Eligible Ind indicates if the crop type is eligible to for plant insurance (Y) or not (N) from cirr_crop_types.is_plant_insrnce_eligible_flag'
;
COMMENT ON COLUMN cuws.crop_commodity.is_other_insurance_eligible_ind IS 'Is Other Insurance Eligible indicates if the crop type is eligible to for other insurance (Y) or not (N) from cirr_crop_types.is_other_insrnce_eligible_flag'
;

UPDATE crop_commodity set 
is_product_insurable_ind = 'N',
is_crop_insurance_eligible_ind = 'N',
is_plant_insurance_eligible_ind = 'N',
is_other_insurance_eligible_ind = 'N';

ALTER TABLE crop_commodity ALTER COLUMN is_product_insurable_ind SET NOT NULL;
ALTER TABLE crop_commodity ALTER COLUMN is_crop_insurance_eligible_ind SET NOT NULL;
ALTER TABLE crop_commodity ALTER COLUMN is_plant_insurance_eligible_ind SET NOT NULL;
ALTER TABLE crop_commodity ALTER COLUMN is_other_insurance_eligible_ind SET NOT NULL;
