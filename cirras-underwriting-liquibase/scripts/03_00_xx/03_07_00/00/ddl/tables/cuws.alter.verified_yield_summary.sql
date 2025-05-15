ALTER TABLE cuws.verified_yield_summary ADD COLUMN insurable_value_hundred_percent numeric(14, 4);
ALTER TABLE cuws.verified_yield_summary ADD COLUMN production_acres numeric(14, 4);

COMMENT ON COLUMN cuws.verified_yield_summary.insurable_value_hundred_percent IS 'Insurable Value Hundred Percent corresponds to cirr_insrnc_prdct_prchses.q_insurable_value'
;

COMMENT ON COLUMN cuws.verified_yield_summary.production_acres IS 'Production Acres is the sum of the harvested acres and appraised acres of the commodity'
;