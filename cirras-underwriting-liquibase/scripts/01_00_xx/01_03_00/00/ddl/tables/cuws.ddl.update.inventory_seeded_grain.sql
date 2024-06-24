


-- Add is_spot_loss_insurable.
ALTER TABLE cuws.inventory_seeded_grain ADD COLUMN is_spot_loss_insurable_ind varchar(1);

COMMENT ON COLUMN cuws.inventory_seeded_grain.is_spot_loss_insurable_ind IS 'Is Spot Loss Insurable Ind determines if the specified planting is insurable for Grain Spot Loss in CIRRAS (Y) or not (N).';

UPDATE inventory_seeded_grain set is_spot_loss_insurable_ind = 'N';

ALTER TABLE inventory_seeded_grain ALTER COLUMN is_spot_loss_insurable_ind SET NOT NULL;

-- Make columns nullable.
ALTER TABLE inventory_seeded_grain ALTER COLUMN crop_commodity_id DROP NOT NULL;
ALTER TABLE inventory_seeded_grain ALTER COLUMN crop_variety_id DROP NOT NULL;
ALTER TABLE inventory_seeded_grain ALTER COLUMN seeded_acres DROP NOT NULL;
