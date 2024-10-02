

CREATE TABLE cuws.yield_meas_unit_conversion(
    yield_meas_unit_conversion_guid     varchar(32)       NOT NULL,
    crop_commodity_id                   numeric(9, 0)     NOT NULL,
    src_yield_meas_unit_type_code       varchar(10)       NOT NULL,
    target_yield_meas_unit_type_code    varchar(10)       NOT NULL,
    version_number                      numeric(2, 0)     NOT NULL,
    effective_crop_year                 numeric(4, 0)     NOT NULL,
    expiry_crop_year                    numeric(4, 0)     NOT NULL,
    conversion_factor                   numeric(14, 4)    NOT NULL,
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(0)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.yield_meas_unit_conversion.yield_meas_unit_conversion_guid IS 'Yield Meas Unit Conversion Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.src_yield_meas_unit_type_code IS 'Src Yield Meas Unit Type Code is the source unit from which the conversion is defined.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.target_yield_meas_unit_type_code IS 'Target Yield Meas Unit Type Code is the target unit to which the conversion is defined.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.version_number IS 'Version Number is the version assigned to the unit conversion, starting from 1.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.effective_crop_year IS 'Effective Crop Year is the crop year the unit conversion becomes effective.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.expiry_crop_year IS 'Expiry Crop Year is the last crop year for which the unit conversion is effective.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.conversion_factor IS 'Conversion Factor is factor per unit that is multiplied against the number of SRC_YIELD_MEAS_UNIT_TYPE_CODE to provide a conversion to the TARGET_YIELD_MEAS_UNIT_TYPE_CODE.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.yield_meas_unit_conversion.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.yield_meas_unit_conversion IS 'The table defines conversions between yield units by commodity.'
;

CREATE INDEX ix_ymuc_cco ON cuws.yield_meas_unit_conversion(crop_commodity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_ymuc_ymutc1 ON cuws.yield_meas_unit_conversion(src_yield_meas_unit_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_ymuc_ymutc2 ON cuws.yield_meas_unit_conversion(target_yield_meas_unit_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.yield_meas_unit_conversion ADD 
    CONSTRAINT pk_ymuc PRIMARY KEY (yield_meas_unit_conversion_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.yield_meas_unit_conversion ADD 
    CONSTRAINT uk_ymuc UNIQUE (crop_commodity_id, src_yield_meas_unit_type_code, target_yield_meas_unit_type_code, version_number) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.yield_meas_unit_conversion ADD CONSTRAINT fk_ymuc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.yield_meas_unit_conversion ADD CONSTRAINT fk_ymuc_ymutc1 
    FOREIGN KEY (src_yield_meas_unit_type_code)
    REFERENCES cuws.yield_meas_unit_type_code(yield_meas_unit_type_code)
;

ALTER TABLE cuws.yield_meas_unit_conversion ADD CONSTRAINT fk_ymuc_ymutc2 
    FOREIGN KEY (target_yield_meas_unit_type_code)
    REFERENCES cuws.yield_meas_unit_type_code(yield_meas_unit_type_code)
;


