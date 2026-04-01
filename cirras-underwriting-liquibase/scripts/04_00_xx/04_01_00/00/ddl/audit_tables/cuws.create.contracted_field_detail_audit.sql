CREATE TABLE cuws.contracted_field_detail_audit(
    contracted_field_detail_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code         varchar(10)       NOT NULL,
    audit_time_stamp                    timestamp(6)      NOT NULL,
    contracted_field_detail_id          numeric(10, 0)    NOT NULL,
    annual_field_detail_id              numeric(10, 0),
    grower_contract_year_id             numeric(10, 0),
    display_order                       numeric(4, 0),
    is_leased_ind                       varchar(1),
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(6)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(6)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.contracted_field_detail_audit.contracted_field_detail_audit_id IS 'Contracted Field Detail Audit Id is the ID of the Contracted Field Detail Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.contracted_field_detail_id IS 'Contracted Field Detail Id is a unique key of a lot from cirr_contracted_lot_detail.contracted_lot_detail_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.grower_contract_year_id IS 'Grower Contract Year Id is a unique record identifier for Grower Contract Year records from cirr_grower_contract_years.grower_contract_year_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.display_order IS 'Display Order is the order of the lot records presented on inventory screens. Entered by the user.'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.is_leased_ind IS 'Is Leased Ind denotes whether the field is Leased (Y) or owned (N)'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.contracted_field_detail_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.contracted_field_detail_audit IS 'Contracted Field Detail Audit table is tha audit table for contracted_field_detail'
;

ALTER TABLE cuws.contracted_field_detail_audit ADD 
    CONSTRAINT pk_cfda PRIMARY KEY (contracted_field_detail_audit_id)
;

ALTER TABLE cuws.contracted_field_detail_audit ADD CONSTRAINT fk_cfda_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


