CREATE TABLE cuws.commodity_type_variety_xref(
    commodity_type_code     varchar(30)      NOT NULL,
    crop_variety_id         numeric(9, 0)    NOT NULL,
    effective_date          date             NOT NULL,
    expiry_date             date             NOT NULL,
    data_sync_trans_date    timestamp(0)     NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp(0)     NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.commodity_type_variety_xref.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.commodity_type_variety_xref.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.commodity_type_variety_xref IS 'The table contains cross reference between varieties and commodity types. Data is taken from CIRR_COMMODITY_TYPE_VRTY_XREF'
;

ALTER TABLE cuws.commodity_type_variety_xref ADD 
    CONSTRAINT pk_ctvx PRIMARY KEY (commodity_type_code, crop_variety_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.commodity_type_variety_xref ADD CONSTRAINT fk_ctvx_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES cuws.commodity_type_code(commodity_type_code)
;

ALTER TABLE cuws.commodity_type_variety_xref ADD CONSTRAINT fk_ctvx_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;


