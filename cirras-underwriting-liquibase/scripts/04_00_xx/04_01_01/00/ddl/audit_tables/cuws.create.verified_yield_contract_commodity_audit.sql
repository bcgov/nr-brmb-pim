CREATE TABLE cuws.verified_yield_contract_commodity_audit(
    verified_yield_contract_commodity_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                   varchar(10)       NOT NULL,
    audit_time_stamp                              timestamp(6)      NOT NULL,
    verified_yield_contract_commodity_guid        varchar(32)       NOT NULL,
    verified_yield_contract_guid                  varchar(32)       NOT NULL,
    crop_commodity_id                             numeric(9, 0),
    commodity_type_code                           varchar(30),
    is_pedigree_ind                               varchar(1),
    harvested_acres                               numeric(14, 4),
    harvested_acres_override                      numeric(14, 4),
    stored_yield_default_unit                     numeric(14, 4),
    sold_yield_default_unit                       numeric(14, 4),
    production_guarantee                          numeric(14, 4),
    harvested_yield                               numeric(14, 4),
    harvested_yield_override                      numeric(14, 4),
    yield_per_acre                                numeric(14, 4),
    create_user                                   varchar(64)       NOT NULL,
    create_date                                   timestamp(0)      NOT NULL,
    update_user                                   varchar(64)       NOT NULL,
    update_date                                   timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.verified_yield_contract_commodity_audit_id IS 'Verified Yield Contract Commodity Audit Id is the ID of the Verified Yield Contract Commodity Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.verified_yield_contract_commodity_guid IS 'Verified Yield Contract Commodity GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.harvested_acres IS 'Harvested Acres is the Harvested Acres for the Commodity from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.harvested_acres_override IS 'Harvested Acres Override is the Harvested Acres for the Commodity entered by the user'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.stored_yield_default_unit IS 'Stored Yield is the yield that is stored on farm for the Commodity from the Declaration of Production sheet, in VERIFIED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units, from DECLARED_YIELD_CONTRACT_COMMODITY'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.sold_yield_default_unit IS 'Sold Yield is the yield that is sold for the Commodity from the Declaration of Production sheet, in VERIFIED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units, from DECLARED_YIELD_CONTRACT_COMMODITY'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.production_guarantee IS 'Production Guarantee is the calculated value from CIRRAS'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.harvested_yield IS 'Harvested Yield is the sum of STORED_YIELD_DEFAULT_UNIT and SOLD_YIELD_DEFAULT_UNIT'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.harvested_yield_override IS 'Harvested Yield Override is the Harvested Yield for the Commodity entered by the user'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.yield_per_acre IS 'Yield Per Acre is a calculated value: Harvested Yield / Harvested Acres. Taking the override values if they exist'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.verified_yield_contract_commodity_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_contract_commodity_audit IS 'Verified Yield Contract Commodity Audit is the audit table for verified_yield_contract_commodity.'
;

ALTER TABLE cuws.verified_yield_contract_commodity_audit ADD 
    CONSTRAINT pk_vycca PRIMARY KEY (verified_yield_contract_commodity_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_contract_commodity_audit ADD CONSTRAINT fk_vycca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


