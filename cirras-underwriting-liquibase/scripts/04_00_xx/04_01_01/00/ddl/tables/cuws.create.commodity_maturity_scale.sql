CREATE TABLE cuws.commodity_maturity_scale(
    commodity_maturity_scale_guid    varchar(32)       NOT NULL,
    crop_commodity_id                numeric(9, 0)     NOT NULL,
    plant_age                        numeric(4, 0)     NOT NULL,
    scale                            numeric(10, 4)    NOT NULL,
    version_number                   numeric(2, 0)     NOT NULL,
    effective_crop_year              numeric(4, 0)     NOT NULL,
    expiry_crop_year                 numeric(4, 0)     NOT NULL,
    create_user                      varchar(64)       NOT NULL,
    create_date                      timestamp(0)      NOT NULL,
    update_user                      varchar(64)       NOT NULL,
    update_date                      timestamp(0)      NOT NULL
) TABLESPACE pg_default
;


COMMENT ON COLUMN cuws.commodity_maturity_scale.commodity_maturity_scale_guid IS 'Commodity Maturity Scale Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.plant_age IS 'Plant Age is the age of the plant'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.scale IS 'Scale is the scale maturity percentage where 1 = 100% for an plant age and commodity'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.version_number IS 'Version Number is the number of the version'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.effective_crop_year IS 'Effective Crop Year is the first year the version is valid'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.expiry_crop_year IS 'Expiry Crop Year is the last year the version is valid'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.commodity_maturity_scale.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.commodity_maturity_scale IS 'The table contains maturity scale percentage by commodity and age'
;

CREATE INDEX ix_cms_cco ON cuws.commodity_maturity_scale(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.commodity_maturity_scale ADD 
    CONSTRAINT pk_cms PRIMARY KEY (commodity_maturity_scale_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.commodity_maturity_scale ADD 
    CONSTRAINT uk_cms UNIQUE (crop_commodity_id, plant_age, version_number) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.commodity_maturity_scale ADD CONSTRAINT fk_cms_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;
