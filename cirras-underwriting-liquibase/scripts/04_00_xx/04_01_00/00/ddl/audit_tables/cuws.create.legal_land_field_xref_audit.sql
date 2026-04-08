CREATE TABLE cuws.legal_land_field_xref_audit(
    legal_land_field_xref_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code       varchar(10)       NOT NULL,
    audit_time_stamp                  timestamp(6)      NOT NULL,
    legal_land_id                     numeric(10, 0)    NOT NULL,
    field_id                          numeric(9, 0)     NOT NULL,
    create_user                       varchar(64)       NOT NULL,
    create_date                       timestamp(6)      NOT NULL,
    update_user                       varchar(64)       NOT NULL,
    update_date                       timestamp(6)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.legal_land_field_xref_audit.legal_land_field_xref_audit_id IS 'Legal Land Field Xref Audit Id is the ID of the Legal Land Field Xref Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.legal_land_id IS 'Legal Land Id is a unique key of a legal land from cirr_legal_land.legal_land_id'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.legal_land_field_xref_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.legal_land_field_xref_audit IS 'The table contains cross references between legal lands and fields from CIRR_LEGAL_LAND_LOT_XREF'
;

ALTER TABLE cuws.legal_land_field_xref_audit ADD 
    CONSTRAINT pk_llfxa PRIMARY KEY (legal_land_field_xref_audit_id)
;

ALTER TABLE cuws.legal_land_field_xref_audit ADD CONSTRAINT fk_llfxa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


