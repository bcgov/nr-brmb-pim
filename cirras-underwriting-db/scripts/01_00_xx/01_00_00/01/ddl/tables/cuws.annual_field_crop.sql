CREATE TABLE cuws.annual_field_crop(
    annual_field_crop_id      numeric(10, 0)    NOT NULL,
    annual_field_detail_id    numeric(10, 0)    NOT NULL,
    crop_commodity_id         numeric(9, 0)     NOT NULL,
    data_sync_trans_date      timestamp(0)      NOT NULL,
    create_user               varchar(64)       NOT NULL,
    create_date               timestamp(0)      NOT NULL,
    update_user               varchar(64)       NOT NULL,
    update_date               timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.annual_field_crop.annual_field_crop_id IS 'Annual Field Crop Id is a unique key of a lot from cirr_annual_lot_crop.annual_lot_crop_id'
;
COMMENT ON COLUMN cuws.annual_field_crop.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id'
;
COMMENT ON COLUMN cuws.annual_field_crop.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.annual_field_crop.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.annual_field_crop.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.annual_field_crop.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.annual_field_crop.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.annual_field_crop.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.annual_field_crop IS 'The table contains all annual field crop records from cirr_annual_lot_crop'
;

CREATE INDEX ix_afc_cco ON cuws.annual_field_crop(crop_commodity_id)
 TABLESPACE pg_default
;

CREATE INDEX ix_afc_afd ON cuws.annual_field_crop(annual_field_detail_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.annual_field_crop ADD 
    CONSTRAINT pk_afc PRIMARY KEY (annual_field_crop_id) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cuws.annual_field_crop ADD 
    CONSTRAINT uk_afc UNIQUE (annual_field_detail_id, crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.annual_field_crop ADD CONSTRAINT fk_afc_afd 
    FOREIGN KEY (annual_field_detail_id)
    REFERENCES cuws.annual_field_detail(annual_field_detail_id)
;

ALTER TABLE cuws.annual_field_crop ADD CONSTRAINT fk_afc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


