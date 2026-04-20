CREATE TABLE cuws.inventory_berries_audit(
    inventory_berries_audit_id      numeric(9, 0)     NOT NULL,
    audit_transaction_type_code     varchar(10)       NOT NULL,
    audit_time_stamp                timestamp(6)      NOT NULL,
    inventory_berries_guid          varchar(32)       NOT NULL,
    inventory_field_guid            varchar(32),
    crop_commodity_id               numeric(9, 0),
    crop_variety_id                 numeric(9, 0),
    plant_insurability_type_code    varchar(10),
    planted_year                    numeric(4, 0),
    planted_acres                   numeric(10, 4),
    mature_equivalent_acres         numeric(10, 4),
    row_spacing                     numeric(4, 0),
    plant_spacing                   numeric(10, 4),
    total_plants                    numeric(10, 0),
    is_quantity_insurable_ind       varchar(1),
    is_plant_insurable_ind          varchar(1),
    bog_id                          varchar(10),
    bog_mowed_date                  date,
    bog_renovated_date              date,
    is_harvested_ind                varchar(1),
    create_user                     varchar(64)       NOT NULL,
    create_date                     timestamp(0)      NOT NULL,
    update_user                     varchar(64)       NOT NULL,
    update_date                     timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_berries_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.inventory_berries_guid IS 'Inventory Berries Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.inventory_berries_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.plant_insurability_type_code IS 'Plant Insurability Type Code is a unique record identifier for plant insurability type records.'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.planted_year IS 'Planted Year was the year when the variety was planted'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.planted_acres IS 'Planted Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.mature_equivalent_acres IS 'Mature Equivalent Acres is the number of ME acres according to the planted year and scale'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.row_spacing IS 'Row spacing is a measure of distance between rows.'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.plant_spacing IS 'Plant spacing is a measure of distance between rows.'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.total_plants IS 'Total plants is a count of the plants of the specified variety planted on the field.'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.is_quantity_insurable_ind IS 'Is Quantity Insured flag determines if the specified crop is quantity insurable (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.is_plant_insurable_ind IS 'Is Plant Insured flag determines if the specified crop is plant insurable (Y) or not (N).'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.bog_id IS 'Bog Id is the id of a cranberry bog used on the inventory contract'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.bog_mowed_date IS 'Bog Mowed Date is the date of when a cranberry bog has been mowed'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.bog_renovated_date IS 'Bog Renovated Date is the date of when a cranberry bog has been renovated'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.is_harvested_ind IS 'Is Harvested Ind indicates if a cranberry bog is being harvested or not'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_berries_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_berries_audit IS 'Inventory Berries Audit is the audit table for inventory_berries'
;

ALTER TABLE cuws.inventory_berries_audit ADD 
    CONSTRAINT pk_iba PRIMARY KEY (inventory_berries_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_berries_audit ADD CONSTRAINT fk_iba_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


