CREATE TABLE cuws.legal_land(
    legal_land_id                  numeric(10, 0)    NOT NULL,
    primary_reference_type_code    varchar(10)       NOT NULL,
    legal_description              varchar(2000),
    legal_short_description        varchar(500),
    other_description              varchar(128),
    active_from_crop_year          numeric(4, 0)     NOT NULL,
    active_to_crop_year            numeric(4, 0),
    data_sync_trans_date           timestamp         NOT NULL,
    create_user                    varchar(64)       NOT NULL,
    create_date                    timestamp         NOT NULL,
    update_user                    varchar(64)       NOT NULL,
    update_date                    timestamp         NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.legal_land.legal_land_id IS 'Legal Land Id is a unique key of a legal land from cirr_legal_land.legal_land_id'
;
COMMENT ON COLUMN cuws.legal_land.primary_reference_type_code IS 'Primary Reference Type Code is a code value that uniquely identifies the record from cirr_legal_land.primary_reference_type_code'
;
COMMENT ON COLUMN cuws.legal_land.legal_description IS 'Legal Description is the description used by the BC Land Survey System for a lot of land. Populated from cirr_legal_land.legal_decription.'
;
COMMENT ON COLUMN cuws.legal_land.legal_short_description IS 'Legal Short Description is a short form of the Legal Description from cirr_legal_land.short_legal_description. '
;
COMMENT ON COLUMN cuws.legal_land.other_description IS 'Other Description is a text description used to describe legal land. Populated from cirr_legal_land.other_description'
;
COMMENT ON COLUMN cuws.legal_land.active_from_crop_year IS 'Active From Crop Year is the first year the legal land is active. Populated from cirr_legal_land.active_from_crop_year'
;
COMMENT ON COLUMN cuws.legal_land.active_to_crop_year IS 'Active To Crop Year is the last year the legal land was active. Populated from cirr_legal_land.active_to_crop_year'
;
COMMENT ON COLUMN cuws.legal_land.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.legal_land.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.legal_land.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.legal_land.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.legal_land.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.legal_land IS 'The table contains all legal lands from CIRR_LEGAL_LAND'
;

ALTER TABLE cuws.legal_land ADD 
    CONSTRAINT pk_ll PRIMARY KEY (legal_land_id) USING INDEX TABLESPACE pg_default 
;

