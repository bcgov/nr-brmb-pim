CREATE TABLE cuws.inventory_contract_commodity_berries(
    inventory_contract_commodity_berries_guid    varchar(32)       NOT NULL,
    inventory_contract_guid                      varchar(32)       NOT NULL,
    crop_commodity_id                            numeric(9, 0)     NOT NULL,
    total_insured_plants                         numeric(10, 0),
    total_uninsured_plants                       numeric(10, 0),
    total_quantity_insured_acres                 numeric(10, 4)    NOT NULL,
    total_quantity_uninsured_acres               numeric(10, 4)    NOT NULL,
    total_plant_insured_acres                    numeric(10, 4),
    total_plant_uninsured_acres                  numeric(10, 4),
    create_user                                  varchar(64)       NOT NULL,
    create_date                                  timestamp(0)      NOT NULL,
    update_user                                  varchar(64)       NOT NULL,
    update_date                                  timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.inventory_contract_commodity_berries_guid IS 'Inventory Contract Commodity Berries Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.total_insured_plants IS 'Total Insured Plants is the number of total plant insured plants for the specific crop.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.total_uninsured_plants IS 'Total Uninsured Plants is the total number of plants which are not plant insurable'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.total_quantity_insured_acres IS 'Total Quantity Insured Acres is the number of total quantity insured acres for the specific crop.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.total_quantity_uninsured_acres IS 'Total Quantity Uninsured Acres is the total number of acres not eligible for quantity insurance'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.total_plant_insured_acres IS 'Total Plant Insured Acres is the number of total plant insured acres for the specific crop.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.total_plant_uninsured_acres IS 'Total Plant Uninsured Acres is the total number of acres not eligible for plant insurance'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_berries.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_contract_commodity_berries IS 'The table contains total insurable and non-insurable plants and acres per year per crop'
;

CREATE INDEX ix_iccb_ico ON cuws.inventory_contract_commodity_berries(inventory_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_iccb_cco ON cuws.inventory_contract_commodity_berries(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_contract_commodity_berries ADD 
    CONSTRAINT pk_iccb PRIMARY KEY (inventory_contract_commodity_berries_guid)
;

ALTER TABLE cuws.inventory_contract_commodity_berries ADD 
    CONSTRAINT uk_iccb UNIQUE (inventory_contract_guid, crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract_commodity_berries ADD CONSTRAINT fk_iccb_ico 
    FOREIGN KEY (inventory_contract_guid)
    REFERENCES cuws.inventory_contract(inventory_contract_guid)
;

ALTER TABLE cuws.inventory_contract_commodity_berries ADD CONSTRAINT if_iccb_ico 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


