CREATE TABLE cuws.verified_yield_grain_basket_audit(
    verified_yield_grain_basket_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code             varchar(10)       NOT NULL,
    audit_time_stamp                        timestamp(6)      NOT NULL,
    verified_yield_grain_basket_guid        varchar(32)       NOT NULL,
    verified_yield_contract_guid            varchar(32),
    basket_value                            numeric(14, 4),
    total_quantity_coverage_value           numeric(14, 4),
    total_coverage_value                    numeric(14, 4),
    harvested_value                         numeric(14, 4),
    comment                                 varchar(200),
    create_user                             varchar(64)       NOT NULL,
    create_date                             timestamp(0)      NOT NULL,
    update_user                             varchar(64)       NOT NULL,
    update_date                             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.verified_yield_grain_basket_audit_id IS 'Verified Yield Grain Basket Audit Id is the ID of the Verified Yield Grain Basket Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.verified_yield_grain_basket_guid IS 'Verified Yield Grain Basket GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.basket_value IS 'Basket Value is copied from CIRR_INSRNC_PRDCT_PRCHSES.gb_coverage_dollars '
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.total_quantity_coverage_value IS 'Total Quantity Coverage is the sum of product.coverage_dollars for all Quantity products.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.total_coverage_value IS 'Total Coverage Value is the sum of basket_value and total_quantity_coverage_value.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.harvested_value IS 'Harvested Value is calculated as SUM(Commodity YTC* Commodity 100%IV)'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.comment IS 'Comment is meant to give a brief summary of the claim'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.create_user IS 'Create User is the user id of the user that created the record.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN cuws.verified_yield_grain_basket_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_grain_basket_audit IS 'Verified Yield Grain Basket Audit is the audit table for verified_yield_grain_basket.'
;

ALTER TABLE cuws.verified_yield_grain_basket_audit ADD 
    CONSTRAINT pk_vygba PRIMARY KEY (verified_yield_grain_basket_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_grain_basket_audit ADD CONSTRAINT fk_vygba_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


