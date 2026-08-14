CREATE TABLE cuws.legal_land_risk_area_xref_audit(
    legal_land_risk_area_xref_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code           varchar(10)       NOT NULL,
    audit_time_stamp                      timestamp(6)      NOT NULL,
    legal_land_id                         numeric(10, 0)    NOT NULL,
    risk_area_id                          numeric(10, 0)    NOT NULL,
    active_from_crop_year                 numeric(4, 0),
    active_to_crop_year                   numeric(4, 0),
    create_user                           varchar(64)       NOT NULL,
    create_date                           timestamp(6)      NOT NULL,
    update_user                           varchar(64)       NOT NULL,
    update_date                           timestamp(6)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.legal_land_risk_area_xref_audit_id IS 'Legal Land Risk Area Xref Audit Id is the ID of the Legal Land Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.legal_land_id IS 'Legal Land Id is a unique key of a legal land from LEGAL_LAND'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.risk_area_id IS 'Risk Area Id is a unique identifier for a RISK AREA generated from a surrogate key'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.active_from_crop_year IS 'Active From Crop Year is the first year the risk area is active for the legal land'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.active_to_crop_year IS 'Active To Crop Year is the last year the risk area was active for the legal land'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.legal_land_risk_area_xref_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.legal_land_risk_area_xref_audit IS 'The table associates risk areas with legal lands.'
;

ALTER TABLE cuws.legal_land_risk_area_xref_audit ADD 
    CONSTRAINT pk_llraxa PRIMARY KEY (legal_land_risk_area_xref_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.legal_land_risk_area_xref_audit ADD CONSTRAINT fk_llraxa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


