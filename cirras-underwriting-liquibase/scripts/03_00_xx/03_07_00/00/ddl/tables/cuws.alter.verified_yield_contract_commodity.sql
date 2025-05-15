ALTER TABLE cuws.verified_yield_contract_commodity ADD COLUMN commodity_type_code varchar(30);

COMMENT ON COLUMN cuws.verified_yield_contract_commodity.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;

CREATE INDEX ix_vycc_ctc  ON cuws.verified_yield_contract_commodity(commodity_type_code)
 TABLESPACE pg_default
;

ALTER TABLE cuws.verified_yield_contract_commodity ADD CONSTRAINT fk_vycc_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES cuws.commodity_type_code(commodity_type_code)
;


ALTER TABLE verified_yield_contract_commodity DROP CONSTRAINT uk_vycc;

ALTER TABLE cuws.verified_yield_contract_commodity ADD 
    CONSTRAINT uk_vycc UNIQUE (verified_yield_contract_guid, crop_commodity_id, is_pedigree_ind, commodity_type_code) USING INDEX TABLESPACE pg_default 
;