CREATE TABLE cuws.field(
    field_id                 numeric(9, 0)    NOT NULL,
    field_label              varchar(28),
    active_from_crop_year    numeric(4, 0)    NOT NULL,
    active_to_crop_year      numeric(4, 0),
    data_sync_trans_date     timestamp(0)     NOT NULL,
    create_user              varchar(64)      NOT NULL,
    create_date              timestamp(0)     NOT NULL,
    update_user              varchar(64)      NOT NULL,
    update_date              timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.field.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.field.field_label IS 'Field Label is a uniquely identifying label for a field from cirr_lots.lot_label'
;
COMMENT ON COLUMN cuws.field.active_from_crop_year IS 'Active From Crop Year is the first year the field is active. Populated from cirr_lots.active_from_crop_year'
;
COMMENT ON COLUMN cuws.field.active_to_crop_year IS 'Active To Crop Year is the last year the field was active. Populated from cirr_lots.active_to_crop_year'
;
COMMENT ON COLUMN cuws.field.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.field.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.field.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.field.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.field.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.field IS 'The table contains all fields (insurable land) from CIRR_LOTS without historical lots (CIRR_LOTS.IS_HISTORICAL_LAND_FLAG = N)'
;

ALTER TABLE cuws.field ADD 
    CONSTRAINT pk_fld PRIMARY KEY (field_id) USING INDEX TABLESPACE pg_default 
;

