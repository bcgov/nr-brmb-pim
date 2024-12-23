ALTER TABLE cuws.verified_yield_summary ADD COLUMN insurable_value_hundred_percent numeric(14, 4);

COMMENT ON COLUMN cuws.verified_yield_summary.insurable_value_hundred_percent IS 'Insurable Value Hundred Percent corresponds to cirr_insrnc_prdct_prchses.q_insurable_value'
;
