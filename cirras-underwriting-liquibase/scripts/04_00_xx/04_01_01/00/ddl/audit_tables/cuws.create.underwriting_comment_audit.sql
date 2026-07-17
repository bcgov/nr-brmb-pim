CREATE TABLE cuws.underwriting_comment_audit(
    underwriting_comment_audit_id     numeric(9, 0)     NOT NULL,
    audit_transaction_type_code       varchar(10)       NOT NULL,
    audit_time_stamp                  timestamp(6)      NOT NULL,
    underwriting_comment_guid         varchar(32)       NOT NULL,
    underwriting_comment_type_code    varchar(10),
    annual_field_detail_id            numeric(10, 0),
    grower_contract_year_id           numeric(10, 0),
    declared_yield_contract_guid      varchar(32),
    verified_yield_summary_guid       varchar(32),
    underwriting_comment              varchar(2000),
    is_forced_ind                     varchar(1),
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp(0)      NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.underwriting_comment_audit.underwriting_comment_audit_id IS 'Underwriting Comment Audit Id is the ID of the Underwriting Comment Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.underwriting_comment_guid IS 'Underwriting Comment GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.underwriting_comment_type_code IS 'Underwriting Comment Type Code is a unique record identifier for underwriting comment type records.'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id. It will be set for all field-level comments.'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.grower_contract_year_id IS 'Grower Contract Year Id is a unique key of a record from grower_contract_year. It will be set for all contract-level comments.'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.declared_yield_contract_guid IS 'Declared Yield Contract Guid links to a record in DECLARED_YIELD_CONTRACT table'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.verified_yield_summary_guid IS 'Verified Yield Summary GUID is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.underwriting_comment IS 'Underwriting Comment is a comment created by a representative regarding a particular aspect of the underwriting process.'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.is_forced_ind IS 'Is Forced Ind denotes whether the comment was forced (Y) or no (N)'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.underwriting_comment_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.underwriting_comment_audit IS 'Underwriting Comment Audit is the audit table for underwriting_comment.'
;

ALTER TABLE cuws.underwriting_comment_audit ADD 
    CONSTRAINT pk_uca PRIMARY KEY (underwriting_comment_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.underwriting_comment_audit ADD CONSTRAINT fk_uc_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


