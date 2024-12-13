CREATE TABLE cuws.verified_yield_summary(
    verified_yield_summary_guid     varchar(32)       NOT NULL,
    verified_yield_contract_guid    varchar(32)       NOT NULL,
    crop_commodity_id               numeric(9, 0)     NOT NULL,
    is_pedigree_ind                 varchar(1)        NOT NULL,
    harvested_yield                 numeric(14, 4),
    harvested_yield_per_acre        numeric(14, 4),
    appraised_yield                 numeric(14, 4),
    assessed_yield                  numeric(14, 4),
    yield_to_count                  numeric(14, 4),
    yield_percent_py                numeric(14, 4),
    production_guarantee            numeric(14, 4),
    probable_yield                  numeric(14, 4),
    create_user                     varchar(64)       NOT NULL,
    create_date                     timestamp(0)      NOT NULL,
    update_user                     varchar(64)       NOT NULL,
    update_date                     timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_summary.verified_yield_summary_guid IS 'Verified Yield Summary GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_summary.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_summary.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.verified_yield_summary.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.verified_yield_summary.harvested_yield IS 'Harvested Yield is either verified_yield_contract_commodity.harvested_yield or verified_yield_contract_commodity.harvested_yield_override if it exists.'
;
COMMENT ON COLUMN cuws.verified_yield_summary.harvested_yield_per_acre IS 'Harvested Yield per Acre matches verified_yield_contract_commodity.yield_per_acre.'
;
COMMENT ON COLUMN cuws.verified_yield_summary.appraised_yield IS 'Appraised Yield is calculated as SUM(verified_yield_amendment.yield_per_acre * acres) where commodity and is_pedigree_ind match and if type is appraised.'
;
COMMENT ON COLUMN cuws.verified_yield_summary.assessed_yield IS 'Assessed Yield is calculated as SUM(verified_yield_amendment.yield_per_ac * acres) where commodity matches and if type is assessed'
;
COMMENT ON COLUMN cuws.verified_yield_summary.yield_to_count IS 'Yield to Count is calculated as the SUM(Harvested Yield, Appraised Yield) from this table'
;
COMMENT ON COLUMN cuws.verified_yield_summary.yield_percent_py IS 'Yield Percent of PY is calculated as (Yield to Count/(Insured Acres * PY)) where Probable Yield is taken from the value of active product matching the commodity from CIRRAS'
;
COMMENT ON COLUMN cuws.verified_yield_summary.production_guarantee IS 'Production Guarantee is taken from CIRR_INSRNC_PRDCT_PRCHSES.Q_PRODUCTION_GUARANTEE'
;
COMMENT ON COLUMN cuws.verified_yield_summary.probable_yield IS 'Probable Yield is taken from CIRR_INSURABLE_CROP_UNITS.PROBABLE_YIELD'
;
COMMENT ON COLUMN cuws.verified_yield_summary.create_user IS 'Create User is the user id of the user that created the record.'
;
COMMENT ON COLUMN cuws.verified_yield_summary.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_summary.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN cuws.verified_yield_summary.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_summary IS 'The table contains verified yield summary data by contract and commodity.'
;

CREATE INDEX ix_vys_vyc ON cuws.verified_yield_summary(verified_yield_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_vys_cco ON cuws.verified_yield_summary(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.verified_yield_summary ADD 
    CONSTRAINT pk_vys PRIMARY KEY (verified_yield_summary_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_summary ADD 
    CONSTRAINT uk_vys UNIQUE (verified_yield_contract_guid, crop_commodity_id, is_pedigree_ind) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_summary ADD CONSTRAINT fk_vys_vyc 
    FOREIGN KEY (verified_yield_contract_guid)
    REFERENCES cuws.verified_yield_contract(verified_yield_contract_guid)
;

ALTER TABLE cuws.verified_yield_summary ADD CONSTRAINT fk_vys_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


