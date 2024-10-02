

CREATE TABLE cuws.declared_yield_contract(
    declared_yield_contract_guid         varchar(32)      NOT NULL,
    contract_id                          numeric(9, 0)    NOT NULL,
    crop_year                            numeric(4, 0)    NOT NULL,
    declaration_of_production_date       date             NOT NULL,
    dop_update_timestamp                 timestamp(0)     NOT NULL,
    dop_update_user                      varchar(64)      NOT NULL,
    entered_yield_meas_unit_type_code    varchar(10)      NOT NULL,
    default_yield_meas_unit_type_code    varchar(10)      NOT NULL,
    grain_from_other_source_ind          varchar(1)       NOT NULL,
    create_user                          varchar(64)      NOT NULL,
    create_date                          timestamp(0)     NOT NULL,
    update_user                          varchar(64)      NOT NULL,
    update_date                          timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_contract.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id.'
;
COMMENT ON COLUMN cuws.declared_yield_contract.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN cuws.declared_yield_contract.declaration_of_production_date IS 'Declaration Of Production Date is the date the Declaration of Production was received.'
;
COMMENT ON COLUMN cuws.declared_yield_contract.dop_update_timestamp IS 'Dop Update Timestamp is the last time any DOP data was changed by the user.'
;
COMMENT ON COLUMN cuws.declared_yield_contract.dop_update_user IS 'Dop Update User is the last user that changed any DOP data.'
;
COMMENT ON COLUMN cuws.declared_yield_contract.entered_yield_meas_unit_type_code IS 'Entered Yield Meas Unit Type Code is the unit in which declared yield has been entered (Bushels or Tonnes).'
;
COMMENT ON COLUMN cuws.declared_yield_contract.default_yield_meas_unit_type_code IS 'Default Yield Meas Unit Type Code is the default unit for the plan (Bushels or Tonnes).'
;
COMMENT ON COLUMN cuws.declared_yield_contract.grain_from_other_source_ind IS 'Grain From Other Source Ind indicates if any grain has been acquired from a source other than inventory.'
;
COMMENT ON COLUMN cuws.declared_yield_contract.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_contract.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_contract.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_contract IS 'The table contains general yield declaration of production data for a contract.'
;

CREATE INDEX ix_dyc_gcy ON cuws.declared_yield_contract(contract_id, crop_year)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyc_ymutc1 ON cuws.declared_yield_contract(entered_yield_meas_unit_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyc_ymutc2 ON cuws.declared_yield_contract(default_yield_meas_unit_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_contract ADD 
    CONSTRAINT pk_dyc PRIMARY KEY (declared_yield_contract_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract ADD 
    CONSTRAINT uk_dyc UNIQUE (contract_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract ADD CONSTRAINT fk_dyc_gcy 
    FOREIGN KEY (contract_id, crop_year)
    REFERENCES cuws.grower_contract_year(contract_id, crop_year)
;

ALTER TABLE cuws.declared_yield_contract ADD CONSTRAINT fk_dyc_ymutc1 
    FOREIGN KEY (entered_yield_meas_unit_type_code)
    REFERENCES cuws.yield_meas_unit_type_code(yield_meas_unit_type_code)
;

ALTER TABLE cuws.declared_yield_contract ADD CONSTRAINT fk_dyc_ymutc2 
    FOREIGN KEY (default_yield_meas_unit_type_code)
    REFERENCES cuws.yield_meas_unit_type_code(yield_meas_unit_type_code)
;


