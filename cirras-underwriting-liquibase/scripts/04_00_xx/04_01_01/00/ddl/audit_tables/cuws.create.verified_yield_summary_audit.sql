CREATE TABLE cuws.verified_yield_summary_audit(
    verified_yield_summary_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code        varchar(10)       NOT NULL,
    audit_time_stamp                   timestamp(6)      NOT NULL,
    verified_yield_summary_guid        varchar(32)       NOT NULL,
    verified_yield_contract_guid       varchar(32),
    crop_commodity_id                  numeric(9, 0),
    is_pedigree_ind                    varchar(1),
    production_acres                   numeric(14, 4),
    harvested_yield                    numeric(14, 4),
    harvested_yield_per_acre           numeric(14, 4),
    appraised_yield                    numeric(14, 4),
    assessed_yield                     numeric(14, 4),
    yield_to_count                     numeric(14, 4),
    yield_percent_py                   numeric(14, 4),
    production_guarantee               numeric(14, 4),
    probable_yield                     numeric(14, 4),
    insurable_value_hundred_percent    numeric(14, 4),
    create_user                        varchar(64)       NOT NULL,
    create_date                        timestamp(0)      NOT NULL,
    update_user                        varchar(64)       NOT NULL,
    update_date                        timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_summary_audit.verified_yield_summary_audit_id IS 'Verified Yield Summary Audit Id is the ID of the Verified Yield Summary Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.verified_yield_summary_guid IS 'Verified Yield Summary GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.production_acres IS 'Production Acres is the sum of the harvested acres and appraised acres of the commodity'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.harvested_yield IS 'Harvested Yield is either verified_yield_contract_commodity.harvested_yield or verified_yield_contract_commodity.harvested_yield_override if it exists.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.harvested_yield_per_acre IS 'Harvested Yield per Acre matches verified_yield_contract_commodity.yield_per_acre.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.appraised_yield IS 'Appraised Yield is calculated as SUM(verified_yield_amendment.yield_per_acre * acres) where commodity and is_pedigree_ind match and if type is appraised.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.assessed_yield IS 'Assessed Yield is calculated as SUM(verified_yield_amendment.yield_per_ac * acres) where commodity matches and if type is assessed'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.yield_to_count IS 'Yield to Count is calculated as the SUM(Harvested Yield, Appraised Yield) from this table'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.yield_percent_py IS 'Yield Percent of PY is calculated as (Yield to Count/(Insured Acres * PY)) where Probable Yield is taken from the value of active product matching the commodity from CIRRAS'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.production_guarantee IS 'Production Guarantee is taken from CIRR_INSRNC_PRDCT_PRCHSES.Q_PRODUCTION_GUARANTEE'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.probable_yield IS 'Probable Yield is taken from CIRR_INSURABLE_CROP_UNITS.PROBABLE_YIELD'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.insurable_value_hundred_percent IS 'Insurable Value Hundred Percent corresponds to cirr_insrnc_prdct_prchses.q_insurable_value'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.create_user IS 'Create User is the user id of the user that created the record.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.update_user IS 'Update User is the user id of the user that updated the record last.'
;
COMMENT ON COLUMN cuws.verified_yield_summary_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_summary_audit IS 'Verified Yield Summary Audit is the audit table for verified_yield_summary.'
;

ALTER TABLE cuws.verified_yield_summary_audit ADD 
    CONSTRAINT pk_vysa PRIMARY KEY (verified_yield_summary_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_summary_audit ADD CONSTRAINT fk_vysa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


