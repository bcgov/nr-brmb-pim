CREATE TABLE cuws.inventory_coverage_total_forage(
    inventory_coverage_total_forage_guid    varchar(32)       NOT NULL,
    inventory_contract_guid                 varchar(32)       NOT NULL,
    crop_commodity_id                       numeric(9, 0),
    plant_insurability_type_code            varchar(10),
    is_unseeded_insurable_ind               varchar(1)        NOT NULL,
    total_field_acres                       numeric(10, 4),
    create_user                             varchar(64)       NOT NULL,
    create_date                             timestamp(0)      NOT NULL,
    update_user                             varchar(64)       NOT NULL,
    update_date                             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_coverage_total_forage.inventory_coverage_total_forage_guid IS 'Inventory Coverage Total Forage GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.inventory_contract_guid IS 'Inventory Contract GUID links to a record in INVENTORY_CONTRACT table'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.plant_insurability_type_code IS 'Plant Insurability Type Code is a unique record identifier for plant insurability type records.'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.is_unseeded_insurable_ind IS 'Is Unseeded Insurable Ind determines if the specified record is the total insurable for Unseeded Coverage(Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.total_field_acres IS 'Total Field Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_coverage_total_forage IS 'The table contains field acres totals per year per commodity or plant insurability, for the forage plan.'
;

CREATE INDEX ix_ictf_ico ON cuws.inventory_coverage_total_forage(inventory_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_ictf_cco ON cuws.inventory_coverage_total_forage(crop_commodity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_ictf_pitc ON cuws.inventory_coverage_total_forage(plant_insurability_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_coverage_total_forage ADD 
    CONSTRAINT pk_ictf PRIMARY KEY (inventory_coverage_total_forage_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_coverage_total_forage ADD 
    CONSTRAINT uk_ictf UNIQUE (inventory_contract_guid, crop_commodity_id, plant_insurability_type_code, is_unseeded_insurable_ind) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_coverage_total_forage ADD CONSTRAINT fk_ictf_ico 
    FOREIGN KEY (inventory_contract_guid)
    REFERENCES cuws.inventory_contract(inventory_contract_guid)
;

ALTER TABLE cuws.inventory_coverage_total_forage ADD CONSTRAINT fk_ictf_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.inventory_coverage_total_forage ADD CONSTRAINT fk_ictf_pict 
    FOREIGN KEY (plant_insurability_type_code)
    REFERENCES cuws.plant_insurability_type_code(plant_insurability_type_code)
;


