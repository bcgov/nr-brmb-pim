CREATE TABLE cuws.inventory_contract_commodity(
    inventory_contract_commodity_guid    varchar(32)       NOT NULL,
    inventory_contract_guid              varchar(32)       NOT NULL,
    crop_commodity_id                    numeric(9, 0),
    total_unseeded_acres                 numeric(10, 4),
    total_unseeded_acres_override        numeric(10, 4),
    total_seeded_acres                   numeric(10, 4),
    total_seeded_acres_override          numeric(10, 4),
    create_user                          varchar(64)       NOT NULL,
    create_date                          timestamp(0)      NOT NULL,
    update_user                          varchar(64)       NOT NULL,
    update_date                          timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_contract_commodity.inventory_contract_commodity_guid IS 'Inventory Contract Commodity GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.total_unseeded_acres IS 'Total Unseeded Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.total_unseeded_acres_override IS 'Total Unseeded Acres Override are the overridden total unseeded acres'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.total_seeded_acres IS 'Total Seeded Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.total_seeded_acres_override IS 'Total Seeded Acres Override are the overriden total seeded acres'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_contract_commodity IS 'The table contains seeded acres totals per year per crop'
;

CREATE INDEX ix_icc_ico ON cuws.inventory_contract_commodity(inventory_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_icc_cco ON cuws.inventory_contract_commodity(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.inventory_contract_commodity ADD 
    CONSTRAINT pk_icc PRIMARY KEY (inventory_contract_commodity_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract_commodity ADD 
    CONSTRAINT uk_icc UNIQUE (inventory_contract_guid, crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract_commodity ADD CONSTRAINT fk_icc_ico 
    FOREIGN KEY (inventory_contract_guid)
    REFERENCES cuws.inventory_contract(inventory_contract_guid)
;

ALTER TABLE cuws.inventory_contract_commodity ADD CONSTRAINT fk_icc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


