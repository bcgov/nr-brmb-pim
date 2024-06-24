CREATE TABLE cuws.annual_field_detail(
    annual_field_detail_id    numeric(10, 0)    NOT NULL,
    legal_land_id             numeric(10, 0),
    field_id                  numeric(9, 0)     NOT NULL,
    crop_year                 numeric(4, 0)     NOT NULL,
    data_sync_trans_date      timestamp(0)      NOT NULL,
    create_user               varchar(64)       NOT NULL,
    create_date               timestamp(0)      NOT NULL,
    update_user               varchar(64)       NOT NULL,
    update_date               timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.annual_field_detail.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id'
;
COMMENT ON COLUMN cuws.annual_field_detail.legal_land_id IS 'Legal Land Id is a unique key of a legal land from cirr_legal_land.legal_land_id'
;
COMMENT ON COLUMN cuws.annual_field_detail.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.annual_field_detail.crop_year IS 'Crop Year is the year the grower is bound by the specified contract. Populated from cirr_annual_lot_detail.crop_year'
;
COMMENT ON COLUMN cuws.annual_field_detail.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.annual_field_detail.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.annual_field_detail.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.annual_field_detail.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.annual_field_detail.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.annual_field_detail IS 'The table contains all annual field detail records from cirr_annual_lot_detail'
;

CREATE INDEX ix_afd_ll ON cuws.annual_field_detail(legal_land_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_afd_fld ON cuws.annual_field_detail(field_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.annual_field_detail ADD 
    CONSTRAINT pk_afd PRIMARY KEY (annual_field_detail_id) USING INDEX TABLESPACE pg_default
;

ALTER TABLE cuws.annual_field_detail ADD 
    CONSTRAINT uk_afd UNIQUE (field_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.annual_field_detail ADD CONSTRAINT fk_afd_ll 
    FOREIGN KEY (legal_land_id)
    REFERENCES cuws.legal_land(legal_land_id)
;

ALTER TABLE cuws.annual_field_detail ADD CONSTRAINT fk_afd_fld 
    FOREIGN KEY (field_id)
    REFERENCES cuws.field(field_id)
;


