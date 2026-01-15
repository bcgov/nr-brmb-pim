CREATE TABLE cuws.declared_yield_field_variety_berries(
    declared_yield_field_variety_berries_guid      varchar(32)       NOT NULL,
    declared_yield_field_commodity_berries_guid    varchar(32)       NOT NULL,
    crop_variety_id                                numeric(9, 0)     NOT NULL,
    planted_acres                                  numeric(10, 4)    NOT NULL,
    sold_shipped_yield                             numeric(14, 4),
    sales_yield                                    numeric(14, 4),
    abandonment_yield                              numeric(14, 4),
    total_production                               numeric(14, 4),
    total_production_override                      numeric(14, 4),
    is_hidden_on_printout_ind                      varchar(1)        NOT NULL,
    create_user                                    varchar(64)       NOT NULL,
    create_date                                    timestamp(0)      NOT NULL,
    update_user                                    varchar(64)       NOT NULL,
    update_date                                    timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.declared_yield_field_variety_berries_guid IS 'Declared Yield Field Variety Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.declared_yield_field_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.crop_variety_id IS 'Crop Variety Id is a unique Id of a variety from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.planted_acres IS 'Planted Acres are the rolled up acres from inventory berries'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.sold_shipped_yield IS 'Sold Shipped Yield is the total pounds of yield sold and shipped for the field and variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.sales_yield IS 'Sales Yield is the total pounds of yield private and direct sold for the field and variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.abandonment_yield IS 'Abandonment Yield is the total pounds of abandonment yield for the field and variety'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.total_production IS 'Total Production is the calculated total pounds of yield by variety and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.total_production_override IS 'Total Production Override is the manually entered total pounds of yield by variety and field'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.is_hidden_on_printout_ind IS 'Is Hidden On Printout Ind determines if the planting is displayed on the printout (N) or not (Y)'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_variety_berries.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_variety_berries IS 'The table contains declaration of production yield for the contract by field and variety for berries'
;

CREATE INDEX ix_dyfvb_cva ON cuws.declared_yield_field_variety_berries(crop_variety_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyfvb_dyfcb ON cuws.declared_yield_field_variety_berries(declared_yield_field_commodity_berries_guid)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_field_variety_berries ADD 
    CONSTRAINT pk_dyfvb PRIMARY KEY (declared_yield_field_variety_berries_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_variety_berries ADD 
    CONSTRAINT uk_dyfvb UNIQUE (declared_yield_field_commodity_berries_guid, crop_variety_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_variety_berries ADD CONSTRAINT fk_dyfvb_dyfcb 
    FOREIGN KEY (declared_yield_field_commodity_berries_guid)
    REFERENCES cuws.declared_yield_field_commodity_berries(declared_yield_field_commodity_berries_guid)
;

ALTER TABLE cuws.declared_yield_field_variety_berries ADD CONSTRAINT fk_dyfvb_cva 
    FOREIGN KEY (crop_variety_id)
    REFERENCES cuws.crop_variety(crop_variety_id)
;


