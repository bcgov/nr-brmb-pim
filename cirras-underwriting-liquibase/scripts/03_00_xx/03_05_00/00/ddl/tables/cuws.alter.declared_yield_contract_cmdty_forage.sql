ALTER TABLE cuws.declared_yield_contract_cmdty_forage
ADD COLUMN total_bales_loads numeric(4,0),
ADD COLUMN weight numeric(14,4),
ADD COLUMN weight_default_unit numeric(14,4),
ADD COLUMN moisture_percent numeric(14,4);


ALTER TABLE cuws.declared_yield_contract_cmdty_forage
DROP COLUMN harvested_acres_override;

ALTER TABLE cuws.declared_yield_contract_cmdty_forage
DROP COLUMN quantity_harvested_tons_override;


COMMENT ON COLUMN cuws.declared_yield_contract_cmdty_forage.total_bales_loads IS 'Total Bales Loads is the number of bales or loads harvested on the field';

COMMENT ON COLUMN cuws.declared_yield_contract_cmdty_forage.weight IS 'Weight is the total weight in the selected units';

COMMENT ON COLUMN cuws.declared_yield_contract_cmdty_forage.weight_default_unit IS 'Weight is the total weight in the default units of the insurance plan';
	
COMMENT ON COLUMN cuws.declared_yield_contract_cmdty_forage.moisture_percent IS 'Moisture Percent is the percentage of water in the crops';