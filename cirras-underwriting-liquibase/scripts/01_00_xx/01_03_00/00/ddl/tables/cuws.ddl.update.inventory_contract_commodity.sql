


-- Remove total_seeded_acres_override.
ALTER TABLE cuws.inventory_contract_commodity DROP COLUMN total_seeded_acres_override;


-- Add is_pedigree_ind and set all rows to N.
ALTER TABLE cuws.inventory_contract_commodity ADD COLUMN is_pedigree_ind varchar(1);

COMMENT ON COLUMN cuws.inventory_contract_commodity.is_pedigree_ind IS 'Is Pedigree Ind determines if the acres is for a pedigree crop (Y) or (N).';

UPDATE inventory_contract_commodity set is_pedigree_ind = 'N';

ALTER TABLE inventory_contract_commodity ALTER COLUMN is_pedigree_ind SET NOT NULL;


-- Add total_spot_loss_acres and set all rows to 0.
ALTER TABLE cuws.inventory_contract_commodity ADD COLUMN total_spot_loss_acres numeric(10,4);

COMMENT ON COLUMN cuws.inventory_contract_commodity.total_spot_loss_acres IS 'Total Spot Loss Acres is the number of seeded acres that are insurable for Grain Spot Loss';

UPDATE inventory_contract_commodity set total_spot_loss_acres = 0;


-- Update unique key.
ALTER TABLE cuws.inventory_contract_commodity DROP CONSTRAINT uk_icc;

ALTER TABLE cuws.inventory_contract_commodity ADD CONSTRAINT uk_icc UNIQUE (inventory_contract_guid, crop_commodity_id, is_pedigree_ind) USING INDEX TABLESPACE pg_default;
