

--remove not nullable from annual_field_detail_id
ALTER TABLE cuws.underwriting_comment ALTER COLUMN annual_field_detail_id DROP NOT NULL;
COMMENT ON COLUMN cuws.underwriting_comment.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id. It will be set for all field-level comments.'
;

--Add new COLUMNS
ALTER TABLE cuws.underwriting_comment ADD COLUMN grower_contract_year_id numeric(10, 0);
ALTER TABLE cuws.underwriting_comment ADD COLUMN declared_yield_contract_guid varchar(32);

COMMENT ON COLUMN cuws.underwriting_comment.grower_contract_year_id IS 'Grower Contract Year Id is a unique key of a record from grower_contract_year. It will be set for all contract-level comments.'
;
COMMENT ON COLUMN cuws.underwriting_comment.declared_yield_contract_guid IS 'Declared Yield Contract Guid links to a record in DECLARED_YIELD_CONTRACT table'
;

CREATE INDEX ix_uc_gcy ON cuws.underwriting_comment(grower_contract_year_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_uc_dyc ON cuws.underwriting_comment(declared_yield_contract_guid)
 TABLESPACE pg_default
;

ALTER TABLE cuws.underwriting_comment ADD CONSTRAINT fk_uc_gcy 
    FOREIGN KEY (grower_contract_year_id)
    REFERENCES cuws.grower_contract_year(grower_contract_year_id)
;

ALTER TABLE cuws.underwriting_comment ADD CONSTRAINT fk_uc_dyc 
    FOREIGN KEY (declared_yield_contract_guid)
    REFERENCES cuws.declared_yield_contract(declared_yield_contract_guid)
;
