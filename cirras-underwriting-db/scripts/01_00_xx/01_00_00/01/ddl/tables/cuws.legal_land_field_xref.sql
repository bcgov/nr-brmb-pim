CREATE TABLE cuws.legal_land_field_xref(
    legal_land_id           numeric(10, 0)    NOT NULL,
    field_id                numeric(9, 0)     NOT NULL,
    data_sync_trans_date    timestamp(0)      NOT NULL,
    create_user             varchar(64)       NOT NULL,
    create_date             timestamp(0)      NOT NULL,
    update_user             varchar(64)       NOT NULL,
    update_date             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.legal_land_field_xref.legal_land_id IS 'Legal Land Id is a unique key of a legal land from cirr_legal_land.legal_land_id'
;
COMMENT ON COLUMN cuws.legal_land_field_xref.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.legal_land_field_xref.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.legal_land_field_xref.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.legal_land_field_xref.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.legal_land_field_xref.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.legal_land_field_xref.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.legal_land_field_xref IS 'The table contains cross references between legal lands and fields from CIRR_LEGAL_LAND_LOT_XREF'
;

ALTER TABLE cuws.legal_land_field_xref ADD 
    CONSTRAINT pk_llfx PRIMARY KEY (legal_land_id, field_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.legal_land_field_xref ADD CONSTRAINT fk_llfx_ll 
    FOREIGN KEY (legal_land_id)
    REFERENCES cuws.legal_land(legal_land_id)
;

ALTER TABLE cuws.legal_land_field_xref ADD CONSTRAINT fk_llfx_fld 
    FOREIGN KEY (field_id)
    REFERENCES cuws.field(field_id)
;


