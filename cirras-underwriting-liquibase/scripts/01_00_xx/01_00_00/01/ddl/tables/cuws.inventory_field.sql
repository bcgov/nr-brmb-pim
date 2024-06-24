CREATE TABLE cuws.inventory_field(
    inventory_field_guid           varchar(32)      NOT NULL,
    insurance_plan_id              numeric(9, 0)    NOT NULL,
    field_id                       numeric(9, 0)    NOT NULL,
    last_year_crop_commodity_id    numeric(9, 0),
    crop_year                      numeric(4, 0)    NOT NULL,
    planting_number                numeric(2, 0)    NOT NULL,
    create_user                    varchar(64)      NOT NULL,
    create_date                    timestamp(0)     NOT NULL,
    update_user                    varchar(64)      NOT NULL,
    update_date                    timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_field.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_field.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.inventory_field.field_id IS 'Field Id is a unique key of a field from cirr_lots.lot_id'
;
COMMENT ON COLUMN cuws.inventory_field.last_year_crop_commodity_id IS 'Last Years Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_field.crop_year IS 'Crop Year is the year for which the crop is insured'
;
COMMENT ON COLUMN cuws.inventory_field.planting_number IS 'Planting Number is the order in which the plantings were added to the field.'
;
COMMENT ON COLUMN cuws.inventory_field.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_field.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_field.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_field.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_field IS 'The table contains data, common for both: inventory seeding intentions and seeded crop inventory '
;

CREATE INDEX ix_if_ip ON cuws.inventory_field(insurance_plan_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_if_fld ON cuws.inventory_field(field_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_if_cco ON cuws.inventory_field(last_year_crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_field ADD 
    CONSTRAINT pk_if PRIMARY KEY (inventory_field_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_field ADD 
    CONSTRAINT uk_if UNIQUE (field_id, crop_year, insurance_plan_id, planting_number) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_field ADD CONSTRAINT fk_if_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;

ALTER TABLE cuws.inventory_field ADD CONSTRAINT fk_if_fld 
    FOREIGN KEY (field_id)
    REFERENCES cuws.field(field_id)
;

ALTER TABLE cuws.inventory_field ADD CONSTRAINT fk_if_cco 
    FOREIGN KEY (last_year_crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


