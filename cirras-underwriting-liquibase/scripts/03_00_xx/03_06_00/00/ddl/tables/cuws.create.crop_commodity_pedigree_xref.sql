CREATE TABLE cuws.crop_commodity_pedigree_xref(
    crop_commodity_id             numeric(9, 0)    NOT NULL,
    crop_commodity_id_pedigree    numeric(9, 0)    NOT NULL,
    create_user                   varchar(64)      NOT NULL,
    create_date                   timestamp(0)     NOT NULL,
    update_user                   varchar(64)      NOT NULL,
    update_date                   timestamp(0)     NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.crop_commodity_pedigree_xref.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.crop_commodity_pedigree_xref.crop_commodity_id_pedigree IS 'Crop Commodity Id Pedigree is the unique identifier for the Crop Type from CROP_COMMODITY that is used for pedigree'
;
COMMENT ON COLUMN cuws.crop_commodity_pedigree_xref.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.crop_commodity_pedigree_xref.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.crop_commodity_pedigree_xref.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.crop_commodity_pedigree_xref.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.crop_commodity_pedigree_xref IS 'The table links a commodity to the same commodity with pedigree'
;

ALTER TABLE cuws.crop_commodity_pedigree_xref ADD 
    CONSTRAINT pk_ccpx PRIMARY KEY (crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.crop_commodity_pedigree_xref ADD 
    CONSTRAINT uk_ccpx UNIQUE (crop_commodity_id, crop_commodity_id_pedigree) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.crop_commodity_pedigree_xref ADD CONSTRAINT fk_ccpx_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.crop_commodity_pedigree_xref ADD CONSTRAINT fk_ccpx_cco_pedigree 
    FOREIGN KEY (crop_commodity_id_pedigree)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;