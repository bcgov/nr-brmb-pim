CREATE TABLE cuws.declared_yield_field_commodity_berries(
    declared_yield_field_commodity_berries_guid    varchar(32)       NOT NULL,
    field_id                                       numeric(9, 0)     NOT NULL,
    crop_commodity_id                              numeric(9, 0)     NOT NULL,
    crop_year                                      numeric(4, 0)     NOT NULL,
    total_production                               numeric(14, 4),
    total_production_override                      numeric(14, 4),
    total_planted_acres                            numeric(10, 4)    NOT NULL,
    total_mature_equivalent_acres                  numeric(10, 4)    NOT NULL,
    create_user                                    varchar(64)       NOT NULL,
    create_date                                    timestamp(0)      NOT NULL,
    update_user                                    varchar(64)       NOT NULL,
    update_date                                    timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.declared_yield_field_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.total_production IS 'Total Production is the calculated total pounds of yield by commdity and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.total_production_override IS 'Total Production Override is the manually entered total pounds of yield by commodity and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.total_planted_acres IS 'Total Planted Acres are the calculated total planted acres from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.total_mature_equivalent_acres IS 'Total Mature Equivalent Acres are the calculated total ME acres from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_commodity_berries.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_commodity_berries IS 'The table contains declaration of production yield for the contract by field and commodity for berries'
;

CREATE INDEX ix_dyfcb_fld ON cuws.declared_yield_field_commodity_berries(field_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyfcb_cco ON cuws.declared_yield_field_commodity_berries(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_field_commodity_berries ADD 
    CONSTRAINT pk_dyfcb PRIMARY KEY (declared_yield_field_commodity_berries_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_commodity_berries ADD 
    CONSTRAINT uk_dyfcb UNIQUE (field_id, crop_commodity_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_commodity_berries ADD CONSTRAINT fk_dyfcb_fld 
    FOREIGN KEY (field_id)
    REFERENCES cuws.field(field_id)
;

ALTER TABLE cuws.declared_yield_field_commodity_berries ADD CONSTRAINT fk_dyfcb_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;
