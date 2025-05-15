CREATE TABLE cuws.verified_yield_grain_basket(
    verified_yield_grain_basket_guid    varchar(32)       NOT NULL,
    verified_yield_contract_guid        varchar(32)       NOT NULL,
    basket_value                        numeric(14, 4)    NOT NULL,
    total_quantity_coverage_value       numeric(14, 4)    NOT NULL,
    total_coverage_value                numeric(14, 4)    NOT NULL,
    harvested_value                     numeric(14, 4)    NOT NULL,
    comment                             varchar(200),
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(0)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.verified_yield_grain_basket.verified_yield_grain_basket_guid IS 'Verified Yield Grain Basket GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.basket_value IS 'Basket Value is copied from CIRR_INSRNC_PRDCT_PRCHSES.gb_coverage_dollars '
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.total_quantity_coverage_value IS 'Total Quantity Coverage is the sum of product.coverage_dollars for all Quantity products.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.total_coverage_value IS 'Total Coverage Value is the sum of basket_value and total_quantity_coverage_value.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.harvested_value IS 'Harvested Value is calculated as SUM(Commodity YTC* Commodity 100%IV)'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.comment IS 'Comment is meant to give a brief summary of the claim'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.create_user IS 'Create User is the user id of the user that created the record.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_grain_basket IS 'The table contains verified yield grain basket data by contract and commodity.'
;

CREATE UNIQUE INDEX ix_vygb_vyc ON cuws.verified_yield_grain_basket(verified_yield_contract_guid)
 TABLESPACE pg_default
;
ALTER TABLE cuws.verified_yield_grain_basket ADD 
    CONSTRAINT pk_vygb PRIMARY KEY (verified_yield_grain_basket_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_grain_basket ADD CONSTRAINT fk_vygb_vyc 
    FOREIGN KEY (verified_yield_contract_guid)
    REFERENCES cuws.verified_yield_contract(verified_yield_contract_guid)
;
