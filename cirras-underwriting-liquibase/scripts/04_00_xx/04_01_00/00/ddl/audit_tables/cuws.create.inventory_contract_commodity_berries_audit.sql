CREATE TABLE cuws.inventory_contract_commodity_berries_audit(
    inventory_contract_commodity_berries_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                      varchar(10)       NOT NULL,
    audit_time_stamp                                 timestamp(6)      NOT NULL,
    inventory_contract_commodity_berries_guid        varchar(32)       NOT NULL,
    inventory_contract_guid                          varchar(32),
    crop_commodity_id                                numeric(9, 0),
    total_insured_plants                             numeric(10, 0),
    total_uninsured_plants                           numeric(10, 0),
    total_quantity_insured_acres                     numeric(10, 4),
    total_quantity_uninsured_acres                   numeric(10, 4),
    total_plant_insured_acres                        numeric(10, 4),
    total_plant_uninsured_acres                      numeric(10, 4),
    create_user                                      varchar(64)       NOT NULL,
    create_date                                      timestamp(0)      NOT NULL,
    update_user                                      varchar(64)       NOT NULL,
    update_date                                      timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.inventory_contract_commodity_berries_audit_id IS 'Inventory Contract Commodity Berries Audit Id is the ID of the Inventory Contract Commodity Berries Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.inventory_contract_commodity_berries_guid IS 'Inventory Contract Commodity Berries Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.total_insured_plants IS 'Total Insured Plants is the number of total plant insured plants for the specific crop.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.total_uninsured_plants IS 'Total Uninsured Plants is the total number of plants which are not plant insurable'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.total_quantity_insured_acres IS 'Total Quantity Insured Acres is the number of total quantity insured acres for the specific crop.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.total_quantity_uninsured_acres IS 'Total Quantity Uninsured Acres is the total number of acres not eligible for quantity insurance'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.total_plant_insured_acres IS 'Total Plant Insured Acres is the number of total plant insured acres for the specific crop.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.total_plant_uninsured_acres IS 'Total Plant Uninsured Acres is the total number of acres not eligible for plant insurance'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_contract_commodity_berries_audit IS 'Inventory Contract Commodity Berries Audit is the audit table for inventory_contract_commodity_berries'
;

ALTER TABLE cuws.inventory_contract_commodity_berries_audit ADD 
    CONSTRAINT pk_iccba PRIMARY KEY (inventory_contract_commodity_berries_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract_commodity_berries_audit ADD CONSTRAINT fk_iccba_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


