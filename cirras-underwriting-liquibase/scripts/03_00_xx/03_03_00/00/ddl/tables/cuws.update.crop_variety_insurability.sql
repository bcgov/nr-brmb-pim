-- Add new column
ALTER TABLE cuws.crop_variety_insurability ADD COLUMN is_grain_unseeded_default_ind varchar(1) DEFAULT 'N' NOT NULL;

COMMENT ON COLUMN cuws.crop_variety_insurability.is_grain_unseeded_default_ind IS 'Is Grain Unseeded Default Ind determines if the specified variety is setting the Unseeded Coverage on a Grain policy (Y) or not (N).'
;

--Set default values
UPDATE crop_variety_insurability set 
is_grain_unseeded_default_ind = 'Y'
WHERE crop_variety_id IN
	( 	
		SELECT v.crop_variety_id
		FROM crop_variety v
		JOIN crop_commodity c ON c.crop_commodity_id = v.crop_commodity_id
		JOIN commodity_type_variety_xref x ON x.crop_variety_id = v.crop_variety_id
		WHERE c.insurance_plan_id = 5
		AND x.commodity_type_code IN ('Spring Annual', 'Silage Corn')
	);

-- Make column not nullable.
ALTER TABLE crop_variety_insurability ALTER COLUMN is_grain_unseeded_default_ind DROP DEFAULT;