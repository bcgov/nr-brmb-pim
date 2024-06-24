

CREATE TABLE cuws.declared_yield_field_rollup(
    declared_yield_field_rollup_guid    varchar(32)       NOT NULL,
    declared_yield_contract_guid        varchar(32)       NOT NULL,
    crop_commodity_id                   numeric(9, 0)     NOT NULL,
    is_pedigree_ind                     varchar(1)        NOT NULL,
    estimated_yield_per_acre_tonnes     numeric(14, 4),
    estimated_yield_per_acre_bushels    numeric(14, 4),
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(0)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_rollup.declared_yield_field_rollup_guid IS 'Declared Yield Field Rollup Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.declared_yield_contract_guid IS 'Declared Yield Contract Guid links to a record in DECLARED_YIELD_CONTRACT table'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.estimated_yield_per_acre_tonnes IS 'Estimated Yield Per Acre Tonnes is the declared estimated amount of yield produced for 1 acre, in Tonnes. It is rolled-up from DECLARED_YIELD_FIELD.ESTIMATED_YIELD_PER_ACRE and converted if necessary.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.estimated_yield_per_acre_bushels IS 'Estimated Yield Per Acre Bushels is the declared estimated amount of yield produced for 1 acre, in Bushels. It is rolled-up from DECLARED_YIELD_FIELD.ESTIMATED_YIELD_PER_ACRE and converted if necessary.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_rollup.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_rollup IS 'The table contains data rolled-up from DECLARED_YIELD_FIELD for the contract by commodity.'
;

CREATE INDEX ix_dyfr_dyc ON cuws.declared_yield_field_rollup(declared_yield_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyfr_cco ON cuws.declared_yield_field_rollup(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_field_rollup ADD 
    CONSTRAINT pk_dyfr PRIMARY KEY (declared_yield_field_rollup_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_rollup ADD 
    CONSTRAINT uk_dyfr UNIQUE (declared_yield_contract_guid, crop_commodity_id, is_pedigree_ind) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_rollup ADD CONSTRAINT fk_dyfr_dyc 
    FOREIGN KEY (declared_yield_contract_guid)
    REFERENCES cuws.declared_yield_contract(declared_yield_contract_guid)
;

ALTER TABLE cuws.declared_yield_field_rollup ADD CONSTRAINT fk_dyfr_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


