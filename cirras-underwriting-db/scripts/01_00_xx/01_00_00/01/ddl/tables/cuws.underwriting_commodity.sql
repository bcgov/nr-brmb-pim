CREATE TABLE cuws.underwriting_commodity(
    underwriting_commodity_id    numeric(10, 0)    NOT NULL,
    insurance_plan_id            numeric(9, 0)     NOT NULL,
    crop_commodity_id            numeric(9, 0)     NOT NULL,
    crop_year                    numeric(4, 0)     NOT NULL,
    inventory_type_code          varchar(20),
    yield_type_code              varchar(20),
    data_sync_trans_date         timestamp(0)      NOT NULL,
    create_user                  varchar(64)       NOT NULL,
    create_date                  timestamp(0)      NOT NULL,
    update_user                  varchar(64)       NOT NULL,
    update_date                  timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.underwriting_commodity.underwriting_commodity_id IS 'Underwriting Commodity Id is a secondary unique identifier used to identify an underwriting commodity table record from cirr_underwriting_commodities.underwriting_commodity_id'
;
COMMENT ON COLUMN cuws.underwriting_commodity.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.underwriting_commodity.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.underwriting_commodity.crop_year IS 'Crop Year is the year in which the crop is insured from cirr_underwriting_commodities.underwriting_crop_year'
;
COMMENT ON COLUMN cuws.underwriting_commodity.inventory_type_code IS 'Inventory Type Code is a unique value that identifies a code from cirr_underwriting_commodities.inventory_type_code'
;
COMMENT ON COLUMN cuws.underwriting_commodity.yield_type_code IS 'Yield Type Code is a unique value that identifies a code from cirr_underwriting_commodities.yield_type_code'
;
COMMENT ON COLUMN cuws.underwriting_commodity.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.underwriting_commodity.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.underwriting_commodity.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.underwriting_commodity.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.underwriting_commodity.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.underwriting_commodity IS 'The table contains all data for inventoriable commodities from cirr_underwriting_commodities'
;

CREATE INDEX ix_uwc_ip ON cuws.underwriting_commodity(insurance_plan_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_uwc_crop_year ON cuws.underwriting_commodity(crop_year)
 TABLESPACE pg_default
;
CREATE INDEX ix_uwc_cco ON cuws.underwriting_commodity(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.underwriting_commodity ADD 
    CONSTRAINT pk_uwc PRIMARY KEY (crop_year, insurance_plan_id, crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.underwriting_commodity ADD 
    CONSTRAINT uk_uwc UNIQUE (underwriting_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.underwriting_commodity ADD CONSTRAINT fk_uwc_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;

ALTER TABLE cuws.underwriting_commodity ADD CONSTRAINT fk_uwc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


