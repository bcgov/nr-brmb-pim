CREATE TABLE cuws.annual_field_crop_audit(
    annual_field_crop_audit_id     numeric(9, 0)     NOT NULL,
    audit_transaction_type_code    varchar(10)       NOT NULL,
    audit_time_stamp               timestamp(6)      NOT NULL,
    annual_field_crop_id           numeric(10, 0)    NOT NULL,
    annual_field_detail_id         numeric(10, 0),
    crop_commodity_id              numeric(9, 0),
    data_sync_trans_date           timestamp(6),
    create_user                    varchar(64)       NOT NULL,
    create_date                    timestamp(6)      NOT NULL,
    update_user                    varchar(64)       NOT NULL,
    update_date                    timestamp(6)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.annual_field_crop_audit.annual_field_crop_audit_id IS 'Annual Field Crop Audit Id is the ID of the Annual Field Crop Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.annual_field_crop_id IS 'Annual Field Crop Id is a unique key of a lot from cirr_annual_lot_crop.annual_lot_crop_id'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.annual_field_crop_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.annual_field_crop_audit IS 'Annual Field Crop Audit table is the audit table for annual_field_crop'
;

ALTER TABLE cuws.annual_field_crop_audit ADD 
    CONSTRAINT pk_afca PRIMARY KEY (annual_field_crop_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.annual_field_crop_audit ADD CONSTRAINT fk_afca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


