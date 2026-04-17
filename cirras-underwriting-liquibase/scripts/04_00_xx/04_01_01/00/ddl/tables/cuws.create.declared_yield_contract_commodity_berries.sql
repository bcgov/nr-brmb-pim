CREATE TABLE cuws.declared_yield_contract_commodity_berries(
    declared_yield_contract_commodity_berries_guid    varchar(32)       NOT NULL,
    declared_yield_contract_guid                      varchar(32)       NOT NULL,
    crop_commodity_id                                 numeric(9, 0)     NOT NULL,
    total_production                                  numeric(14, 4),
    total_production_override                         numeric(14, 4),
    total_planted_acres                               numeric(10, 4)    NOT NULL,
    total_mature_equivalent_acres                     numeric(10, 4)    NOT NULL,
    total_sold_shipped_yield                          numeric(14, 4),
    total_sales_yield                                 numeric(14, 4),
    total_abandonment_yield                           numeric(14, 4),
    create_user                                       varchar(64)       NOT NULL,
    create_date                                       timestamp(0)      NOT NULL,
    update_user                                       varchar(64)       NOT NULL,
    update_date                                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.declared_yield_contract_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.declared_yield_contract_guid IS 'Declared Yield Contract GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_production IS 'Total Production is the calculated total pounds of yield by contract and commodity'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_production_override IS 'Total Production Override is the manually entered total pounds of yield by contract and commodity'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_planted_acres IS 'Total Planted Acres are the calculated total planted acres from field commodity'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_mature_equivalent_acres IS 'Total Mature Equivalent Acres are the calculated total ME acres from field commodity'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_sold_shipped_yield IS 'Total Sold Shipped Yield is the calculated total pounds of yield sold and shipped from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_sales_yield IS 'Total Sales Yield is the calculated total pounds of yield private and direct sold from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.total_abandonment_yield IS 'Total Abandonment Yield is the calculated total pounds of abandonment yield from field variety'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_contract_commodity_berries IS 'The table contains declaration of production yield for the contract by commodity for berries'
;

CREATE INDEX ix_dyccb_dyc ON cuws.declared_yield_contract_commodity_berries(declared_yield_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyccb_cco ON cuws.declared_yield_contract_commodity_berries(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_contract_commodity_berries ADD 
    CONSTRAINT pk_dyccb PRIMARY KEY (declared_yield_contract_commodity_berries_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_commodity_berries ADD 
    CONSTRAINT uk_dyccb UNIQUE (declared_yield_contract_guid, crop_commodity_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_commodity_berries ADD CONSTRAINT fk_dyccb_dyc 
    FOREIGN KEY (declared_yield_contract_guid)
    REFERENCES cuws.declared_yield_contract(declared_yield_contract_guid)
;

ALTER TABLE cuws.declared_yield_contract_commodity_berries ADD CONSTRAINT fk_dyccb_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;
