CREATE TABLE cuws.contracted_field_detail(
    contracted_field_detail_id          numeric(10, 0)    NOT NULL,
    annual_field_detail_id              numeric(10, 0)    NOT NULL,
    grower_contract_year_id             numeric(10, 0)    NOT NULL,
    underseeded_to_crop_commodity_id    numeric(9, 0),
    display_order                       numeric(4, 0)     NOT NULL,
    underseeded_acres                   numeric(10, 4),
    data_sync_trans_date                timestamp(0)      NOT NULL,
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(0)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.contracted_field_detail.contracted_field_detail_id IS 'Contracted Field Detail Id is a unique key of a lot from cirr_contracted_lot_detail.contracted_lot_detail_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail.grower_contract_year_id IS 'Grower Contract Year Id is a unique record identifier for Grower Contract Year records from cirr_grower_contract_years.grower_contract_year_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail.underseeded_to_crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.contracted_field_detail.display_order IS 'Display Order is the order of the lot records presented on inventory screens. Entered by the user.'
;
COMMENT ON COLUMN cuws.contracted_field_detail.underseeded_acres IS 'Underseeded Acres is the number of acres utilized by the underseeded crop. Entered by the user'
;
COMMENT ON COLUMN cuws.contracted_field_detail.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.contracted_field_detail.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.contracted_field_detail.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.contracted_field_detail.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.contracted_field_detail.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.contracted_field_detail IS 'The table contains all annual contracted field detail records from cirr_contracted_lot_detail'
;

CREATE INDEX ix_cfd_afd ON cuws.contracted_field_detail(annual_field_detail_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_cfd_gcy ON cuws.contracted_field_detail(grower_contract_year_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_cfd_cco ON cuws.contracted_field_detail(underseeded_to_crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.contracted_field_detail ADD 
    CONSTRAINT pk_cfd PRIMARY KEY (contracted_field_detail_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.contracted_field_detail ADD 
    CONSTRAINT uk_cfd UNIQUE (grower_contract_year_id, annual_field_detail_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.contracted_field_detail ADD CONSTRAINT fk_cfd_afd 
    FOREIGN KEY (annual_field_detail_id)
    REFERENCES cuws.annual_field_detail(annual_field_detail_id)
;

ALTER TABLE cuws.contracted_field_detail ADD CONSTRAINT fk_cfd_gcy 
    FOREIGN KEY (grower_contract_year_id)
    REFERENCES cuws.grower_contract_year(grower_contract_year_id)
;

ALTER TABLE cuws.contracted_field_detail ADD CONSTRAINT fk_cfd_cco 
    FOREIGN KEY (underseeded_to_crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


