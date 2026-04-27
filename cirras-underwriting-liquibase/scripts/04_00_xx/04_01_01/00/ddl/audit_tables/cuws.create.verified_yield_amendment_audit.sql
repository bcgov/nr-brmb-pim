CREATE TABLE cuws.verified_yield_amendment_audit(
    verified_yield_amendment_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code          varchar(10)       NOT NULL,
    audit_time_stamp                     timestamp(6)      NOT NULL,
    verified_yield_amendment_guid        varchar(32)       NOT NULL,
    verified_yield_amendment_code        varchar(10),
    verified_yield_contract_guid         varchar(32),
    crop_commodity_id                    numeric(9, 0),
    crop_variety_id                      numeric(9, 0),
    is_pedigree_ind                      varchar(1),
    field_id                             numeric(9, 0),
    yield_per_acre                       numeric(14, 4),
    acres                                numeric(14, 4),
    rationale                            varchar(200),
    create_user                          varchar(64)       NOT NULL,
    create_date                          timestamp(0)      NOT NULL,
    update_user                          varchar(64)       NOT NULL,
    update_date                          timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.verified_yield_amendment_audit.verified_yield_amendment_audit_id IS 'Verified Yield Amendment Audit Id is the ID of the Verified Yield Amendment Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.verified_yield_amendment_guid IS 'Verified Yield Amendment GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.verified_yield_amendment_code IS 'Verified Yield Amendment Code is a code value that uniquely identifies a record.'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.verified_yield_contract_guid IS 'Verified Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.yield_per_acre IS 'Yield Per Acre is a user entered value for yield per acre by commodity and field'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.acres IS 'Acres is the user entered value for acres by commodity and field'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.rationale IS 'Rationale is the explanation for the amendement like source or reason'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.verified_yield_amendment_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.verified_yield_amendment_audit IS 'Verified Yield Amendment Audit is the audit table for verified_yield_amendment.'
;

ALTER TABLE cuws.verified_yield_amendment_audit ADD 
    CONSTRAINT pk_vyaa PRIMARY KEY (verified_yield_amendment_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.verified_yield_amendment_audit ADD CONSTRAINT fk_vyaa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


