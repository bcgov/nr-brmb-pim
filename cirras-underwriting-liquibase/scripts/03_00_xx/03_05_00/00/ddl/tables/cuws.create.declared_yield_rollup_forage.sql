CREATE TABLE cuws.declared_yield_rollup_forage(
    declared_yield_rollup_forage_guid    varchar(32)       NOT NULL,
    declared_yield_contract_guid         varchar(32)       NOT NULL,
    commodity_type_code                  varchar(30)       NOT NULL,
    total_field_acres                    numeric(14, 4),
    total_bales_loads                    numeric(4, 0),
    harvested_acres                      numeric(14, 4),
    quantity_harvested_tons              numeric(14, 4),
    yield_per_acre                       numeric(14, 4),
    create_user                          varchar(64)       NOT NULL,
    create_date                          timestamp(0)      NOT NULL,
    update_user                          varchar(64)       NOT NULL,
    update_date                          timestamp(0)      NOT NULL
) TABLESPACE pg_default
;


COMMENT ON COLUMN cuws.declared_yield_rollup_forage.declared_yield_rollup_forage_guid IS 'Declared Yield Rollup Forage Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.total_field_acres IS 'Total field Acres is the total quantity insured field acres from inventory seeded forage by comodity type'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.total_bales_loads IS 'Total Bales Loads is the number of bales or loads harvested on the field'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.harvested_acres IS 'Harvested Acres is the Harvested Acres for the Commodity from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.quantity_harvested_tons IS 'Quantity Harvested Tons is a calculated value from DOP and always in tons'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.yield_per_acre IS 'Yield Per Acre is a calculated value: Quantity Harvested / Harvested Acres. Taking the override values if they exist'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_rollup_forage.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_rollup_forage IS 'The table contains declaration of production yield for the contract by commodity for forage'
;

CREATE INDEX ix_dyrf_ctc ON cuws.declared_yield_rollup_forage(commodity_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyrf_dyc ON cuws.declared_yield_rollup_forage(declared_yield_contract_guid)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_dyrf ON cuws.declared_yield_rollup_forage(declared_yield_contract_guid, commodity_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_rollup_forage ADD 
    CONSTRAINT pk_dyrf PRIMARY KEY (declared_yield_rollup_forage_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_rollup_forage ADD CONSTRAINT fk_dyrf_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES cuws.commodity_type_code(commodity_type_code)
;

ALTER TABLE cuws.declared_yield_rollup_forage ADD CONSTRAINT fk_dyrf_dyc 
    FOREIGN KEY (declared_yield_contract_guid)
    REFERENCES cuws.declared_yield_contract(declared_yield_contract_guid)
;


