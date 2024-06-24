CREATE TABLE cuws.crop_commodity(
    crop_commodity_id            numeric(9, 0)    NOT NULL,
    insurance_plan_id            numeric(9, 0)    NOT NULL,
    commodity_name               varchar(50)      NOT NULL,
    short_label                  varchar(5),
    plant_duration_type_code     varchar(10),
    is_inventory_crop_ind        varchar(1)       NOT NULL,
    is_yield_crop_ind            varchar(1)       NOT NULL,
    is_underwriting_crop_ind     varchar(1)       NOT NULL,
    yield_meas_unit_type_code    varchar(10),
    yield_decimal_precision      numeric(2, 0),
    effective_date               date             NOT NULL,
    expiry_date                  date             NOT NULL,
    data_sync_trans_date         timestamp(0)     NOT NULL,
    create_user                  varchar(64)      NOT NULL,
    create_date                  timestamp(0)     NOT NULL,
    update_user                  varchar(64)      NOT NULL,
    update_date                  timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.crop_commodity.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.crop_commodity.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.crop_commodity.commodity_name IS 'Commodity Name is the name of the commodity from cirr_crop_types.name'
;
COMMENT ON COLUMN cuws.crop_commodity.short_label IS 'Short Label is a short acronym to display to the user when the Name field is too long from cirr_crop_types.short_label'
;
COMMENT ON COLUMN cuws.crop_commodity.plant_duration_type_code IS 'Plant Duration shows whether the plan is annual or perennial from cirr_crop_types.plant_duration_type_code'
;
COMMENT ON COLUMN cuws.crop_commodity.is_inventory_crop_ind IS 'Is Inventory Crop Ind indicates if the crop type can be inventoried (Y) or not (N) from cirr_crop_types.is_inventory_crop_flag'
;
COMMENT ON COLUMN cuws.crop_commodity.is_yield_crop_ind IS 'Is Yield Crop Ind indicates if the crop type can have yield (Y) or not (N) from cirr_crop_types.is_yield_crop_flag'
;
COMMENT ON COLUMN cuws.crop_commodity.is_underwriting_crop_ind IS 'Is Underwriting Crop Ind indicates if the crop type can have an UW Commodity record (Y) or not (N) from cirr_crop_types.is_underwriting_crop_flag'
;
COMMENT ON COLUMN cuws.crop_commodity.yield_meas_unit_type_code IS 'Yield Meas Unit Type Code is a unique value that identifies a code from cirr_crop_types.yield_meas_unit_type_code'
;
COMMENT ON COLUMN cuws.crop_commodity.yield_decimal_precision IS 'Yield Decimal Precision is the precision used to for displaying yield from  cirr_crop_types.yield_decimal_precision'
;
COMMENT ON COLUMN cuws.crop_commodity.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.crop_commodity.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.crop_commodity.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.crop_commodity.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.crop_commodity.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.crop_commodity.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.crop_commodity.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.crop_commodity IS 'The table contains all  commodities. Data is taken from CIRR_CROP_TYPES where crpt_crpt_id is null '
;

CREATE INDEX ix_cco_ip ON cuws.crop_commodity(insurance_plan_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.crop_commodity ADD 
    CONSTRAINT pk_cco PRIMARY KEY (crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.crop_commodity ADD CONSTRAINT fk_cco_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;


