\qecho Create Product table

CREATE TABLE cuws.product(
    product_id                 numeric(9, 0)     NOT NULL,
    policy_id                  numeric(9, 0)     NOT NULL,
    crop_commodity_id          numeric(9, 0)     NOT NULL,
    commodity_coverage_code    varchar(10)       NOT NULL,
    product_status_code        varchar(16)       NOT NULL,
    deductible_level           numeric(3, 0),
    production_guarantee       numeric(14, 4),
    probable_yield             numeric(14, 4),
    insured_by_meas_type       varchar(10)       NOT NULL,
    data_sync_trans_date       timestamp(0)      NOT NULL,
    create_user                varchar(64)       NOT NULL,
    create_date                timestamp(0)      NOT NULL,
    update_user                varchar(64)       NOT NULL,
    update_date                timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.product.product_id IS 'Product Id is a unique key for a Product from cirr_insrnc_prdct_prchses.ipp_id'
;
COMMENT ON COLUMN cuws.product.policy_id IS 'Policy Id is a unique key of a policy from cirr_insrnc_prdct_prchses.ipl_ipl_id '
;
COMMENT ON COLUMN cuws.product.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.product.commodity_coverage_code IS 'Commodity Coverage Code is a unique code for a coverage from cirr_crop_coverages.cc_type'
;
COMMENT ON COLUMN cuws.product.product_status_code IS 'Product Status Code is the status code of the product from cirr_insrnc_prdct_prchses.vippippst_ipp_status_type_code'
;
COMMENT ON COLUMN cuws.product.deductible_level IS 'Deductible Level is the deductible, expressed as a percent, at which the product is insured, from cirr_deductible_rates.ded_level'
;
COMMENT ON COLUMN cuws.product.production_guarantee IS 'Production Guarantee is the guaranteed production this coverage provides for from cirr_insrnc_prdct_prchses.q_production_guarantee'
;
COMMENT ON COLUMN cuws.product.probable_yield IS 'Probable Yield is the calculated probable yield of the commodity and coverage, by unit or acre, from cirr_insurable_crop_units.probable_yield'
;
COMMENT ON COLUMN cuws.product.insured_by_meas_type IS 'Insured By Meas Type determines how the coverage is calculated (ACRES, UNITS, UNKNOWN), based on cirr_crop_coverage_annual.crop_meas_method_code'
;
COMMENT ON COLUMN cuws.product.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.product.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.product.create_date IS 'Create Date is the date when the record was created'
;
COMMENT ON COLUMN cuws.product.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.product.update_date IS 'Update Date is the date when the record was updated last'
;
COMMENT ON TABLE cuws.product IS 'The table contains products from CIRR_INSRNC_PRDCT_PRCHSES'
;

CREATE INDEX ix_prd_pl ON cuws.product(policy_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_prd_cco ON cuws.product(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.product ADD 
    CONSTRAINT pk_prd PRIMARY KEY (product_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.product ADD CONSTRAINT fk_prd_pl 
    FOREIGN KEY (policy_id)
    REFERENCES cuws.policy(policy_id)
;

ALTER TABLE cuws.product ADD CONSTRAINT fk_prd_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


