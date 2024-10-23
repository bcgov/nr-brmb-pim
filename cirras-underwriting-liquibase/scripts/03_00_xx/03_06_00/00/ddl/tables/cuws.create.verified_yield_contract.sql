CREATE TABLE cuws.verified_yield_contract(
    verified_yield_contract_guid         varchar(32)      NOT NULL,
    contract_id                          numeric(9, 0)    NOT NULL,
    crop_year                            numeric(4, 0)    NOT NULL,
    declared_yield_contract_guid         varchar(32)      NOT NULL,
    default_yield_meas_unit_type_code    varchar(10)      NOT NULL,
    verified_yield_update_timestamp      timestamp(0)     NOT NULL,
    verified_yield_update_user           varchar(64)      NOT NULL,
    create_user                          varchar(64)      NOT NULL,
    create_date                          timestamp(0)     NOT NULL,
    update_user                          varchar(64)      NOT NULL,
    update_date                          timestamp(0)     NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.verified_yield_contract.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id (also in POLICY.CONTRACT_ID)'
;
COMMENT ON COLUMN cuws.verified_yield_contract.crop_year IS 'Crop Year is the year the grower is bound by the specified contract from cirr_grower_contract_years.crop_year'
;
COMMENT ON COLUMN cuws.verified_yield_contract.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract.default_yield_meas_unit_type_code IS 'Yield Meas Unit Type Code is a unique record identifier for yield meas unit type records.'
;
COMMENT ON COLUMN cuws.verified_yield_contract.verified_yield_update_timestamp IS 'Verified Yield Update Timestamp is the last time any DOP data was changed by the user.'
;
COMMENT ON COLUMN cuws.verified_yield_contract.verified_yield_update_user IS 'Verified Yield Update User is the last user that changed any Verified Yield data.'
;
COMMENT ON COLUMN cuws.verified_yield_contract.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_contract.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.verified_yield_contract.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_contract IS 'The table contains general verified yield data for a contract.'
;

CREATE INDEX ix_vyc_gcy ON cuws.verified_yield_contract(contract_id, crop_year)
 TABLESPACE pg_default
;
CREATE INDEX ix_vyc_dyc ON cuws.verified_yield_contract(declared_yield_contract_guid)
;
CREATE INDEX ix_vyc_ymutc ON cuws.verified_yield_contract(default_yield_meas_unit_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.verified_yield_contract ADD 
    CONSTRAINT pk_vyc PRIMARY KEY (verified_yield_contract_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_contract ADD 
    CONSTRAINT uk_vyc UNIQUE (contract_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_contract ADD CONSTRAINT fk_vyc_gcy 
    FOREIGN KEY (contract_id, crop_year)
    REFERENCES cuws.grower_contract_year(contract_id, crop_year)
;

ALTER TABLE cuws.verified_yield_contract ADD CONSTRAINT fk_vyc_ymutc 
    FOREIGN KEY (default_yield_meas_unit_type_code)
    REFERENCES cuws.yield_meas_unit_type_code(yield_meas_unit_type_code)
;

ALTER TABLE cuws.verified_yield_contract ADD CONSTRAINT fk_vyc_dyc 
    FOREIGN KEY (declared_yield_contract_guid)
    REFERENCES cuws.declared_yield_contract(declared_yield_contract_guid)
;


