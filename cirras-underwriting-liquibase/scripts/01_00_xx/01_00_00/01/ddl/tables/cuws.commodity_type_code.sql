CREATE TABLE cuws.commodity_type_code(
    commodity_type_code     varchar(30)      NOT NULL,
    crop_commodity_id       numeric(9, 0)    NOT NULL,
    description             varchar(100)     NOT NULL,
    effective_date          date             NOT NULL,
    expiry_date             date             NOT NULL,
    data_sync_trans_date    timestamp(0)     NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp(0)     NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.commodity_type_code.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.commodity_type_code.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.commodity_type_code.description IS 'Description is the long description associated with the code from cirr_commodity_type_code.description'
;
COMMENT ON COLUMN cuws.commodity_type_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.commodity_type_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.commodity_type_code.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.commodity_type_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.commodity_type_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.commodity_type_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.commodity_type_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.commodity_type_code IS 'The table contains all commodity types. Data is taken from CIRR_COMMODITY_TYPE_CODE '
;

CREATE INDEX ix_ctc_cco ON cuws.commodity_type_code(crop_commodity_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.commodity_type_code ADD 
    CONSTRAINT pk_ctc PRIMARY KEY (commodity_type_code) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.commodity_type_code ADD CONSTRAINT fk_ctc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


