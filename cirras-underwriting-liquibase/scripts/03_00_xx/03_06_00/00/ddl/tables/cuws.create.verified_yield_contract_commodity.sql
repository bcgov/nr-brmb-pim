CREATE TABLE cuws.verified_yield_contract_commodity(
    verified_yield_contract_commodity_guid    varchar(32)       NOT NULL,
    verified_yield_contract_guid              varchar(32)       NOT NULL,
    crop_commodity_id                         numeric(9, 0)     NOT NULL,
    harvested_acres                           numeric(14, 4),
    harvested_acres_override                  numeric(14, 4),
    stored_yield_default_unit                 numeric(14, 4),
    sold_yield_default_unit                   numeric(14, 4),
    production_guarantee                      numeric(14, 4),
    harvested_yield                           numeric(14, 4),
    harvested_yield_override                  numeric(14, 4),
    yield_per_acre                            numeric(14, 4),
    create_user                               varchar(64)       NOT NULL,
    create_date                               timestamp(0)      NOT NULL,
    update_user                               varchar(64)       NOT NULL,
    update_date                               timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_contract_commodity.verified_yield_contract_commodity_guid IS 'Verified Yield Contract Commodity GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.harvested_acres IS 'Harvested Acres is the Harvested Acres for the Commodity from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.harvested_acres_override IS 'Harvested Acres Override is the Harvested Acres for the Commodity entered by the user'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.stored_yield_default_unit IS 'Stored Yield is the yield that is stored on farm for the Commodity from the Declaration of Production sheet, in VERIFIED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units, from DECLARED_YIELD_CONTRACT_COMMODITY'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.sold_yield_default_unit IS 'Sold Yield is the yield that is sold for the Commodity from the Declaration of Production sheet, in VERIFIED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units, from DECLARED_YIELD_CONTRACT_COMMODITY'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.production_guarantee IS 'Production Guarantee is the calculated value from CIRRAS'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.harvested_yield IS 'Harvested Yield is the sum of STORED_YIELD_DEFAULT_UNIT and SOLD_YIELD_DEFAULT_UNIT'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.harvested_yield_override IS 'Harvested Yield Override is the Harvested Yield for the Commodity entered by the user'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.yield_per_acre IS 'Yield Per Acre is a calculated value: Harvested Yield / Harvested Acres. Taking the override values if they exist'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_contract_commodity IS 'The table contains verified yield data by contract and commodity.'
;

CREATE INDEX ix_vycc_cco ON cuws.verified_yield_contract_commodity(crop_commodity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_vycc_vyc ON cuws.verified_yield_contract_commodity(verified_yield_contract_guid)
;
CREATE UNIQUE INDEX uk_vycc ON cuws.verified_yield_contract_commodity(verified_yield_contract_guid, crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.verified_yield_contract_commodity ADD 
    CONSTRAINT pk_vycc PRIMARY KEY (verified_yield_contract_commodity_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_contract_commodity ADD CONSTRAINT fk_vycc_vyc 
    FOREIGN KEY (verified_yield_contract_guid)
    REFERENCES cuws.verified_yield_contract(verified_yield_contract_guid)
;

ALTER TABLE cuws.verified_yield_contract_commodity ADD CONSTRAINT fk_vycc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


