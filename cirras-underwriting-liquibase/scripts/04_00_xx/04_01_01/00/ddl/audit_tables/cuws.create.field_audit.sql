CREATE TABLE cuws.field_audit(
    field_audit_id                 numeric(9, 0)    NOT NULL,
    audit_transaction_type_code    varchar(10)      NOT NULL,
    audit_time_stamp               timestamp(6)     NOT NULL,
    field_id                       numeric(9, 0)    NOT NULL,
    field_label                    varchar(28),
    active_from_crop_year          numeric(4, 0),
    active_to_crop_year            numeric(4, 0),
    location                       varchar(128),
    create_user                    varchar(64)      NOT NULL,
    create_date                    timestamp(0)     NOT NULL,
    update_user                    varchar(64)      NOT NULL,
    update_date                    timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.field_audit.field_audit_id IS 'Field Audit Id is the ID of the Field Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.field_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.field_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.field_audit.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.field_audit.field_label IS 'Field Label is a uniquely identifying label for a field from cirr_lots.lot_label'
;
COMMENT ON COLUMN cuws.field_audit.active_from_crop_year IS 'Active From Crop Year is the first year the field is active. Populated from cirr_lot.active_from_crop_year'
;
COMMENT ON COLUMN cuws.field_audit.active_to_crop_year IS 'Active To Crop Year is the last year the field was active. Populated from cirr_lots.active_to_crop_year'
;
COMMENT ON COLUMN cuws.field_audit.location IS 'Location is the description of the location of the field.'
;
COMMENT ON COLUMN cuws.field_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.field_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.field_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.field_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.field_audit IS 'Field Audit is the audit table for field'
;

ALTER TABLE cuws.field_audit ADD 
    CONSTRAINT pk_flda PRIMARY KEY (field_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.field_audit ADD CONSTRAINT fk_flda_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


