

CREATE TABLE cuws.declared_yield_contract_commodity(
    declared_yield_contract_commodity_guid    varchar(32)       NOT NULL,
    declared_yield_contract_guid              varchar(32)       NOT NULL,
    crop_commodity_id                         numeric(9, 0)     NOT NULL,
    is_pedigree_ind                           varchar(1)        NOT NULL,
    harvested_acres                           numeric(14, 4),
    stored_yield                              numeric(14, 4),
    stored_yield_default_unit                 numeric(14, 4),
    sold_yield                                numeric(14, 4),
    sold_yield_default_unit                   numeric(14, 4),
    grade_modifier_type_code                  varchar(10),
    create_user                               varchar(64)       NOT NULL,
    create_date                               timestamp(0)      NOT NULL,
    update_user                               varchar(64)       NOT NULL,
    update_date                               timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_contract_commodity.declared_yield_contract_commodity_guid IS 'Declared Yield Contract Commodity Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.declared_yield_contract_guid IS 'Declared Yield Contract Guid links to a record in DECLARED_YIELD_CONTRACT table'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.harvested_acres IS 'Harvested Acres is the Harvested Acres for the Commodity from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.stored_yield IS 'Stored Yield is the yield that is stored on farm for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.ENTERED_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.stored_yield_default_unit IS 'Stored Yield is the yield that is stored on farm for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.sold_yield IS 'Sold Yield is the yield that is sold for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.ENTERED_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.sold_yield_default_unit IS 'Sold Yield is the yield that is sold for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.grade_modifier_type_code IS 'Grade Modifier Type Code is the grade of the yield from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_contract_commodity IS 'The table contains declaration of production yield for the contract by commodity.'
;

CREATE INDEX ix_dycc_dyc ON cuws.declared_yield_contract_commodity(declared_yield_contract_guid)
 TABLESPACE pg_default
;
CREATE INDEX ix_dycc_cco ON cuws.declared_yield_contract_commodity(crop_commodity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_dycc_gmtc ON cuws.declared_yield_contract_commodity(grade_modifier_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_contract_commodity ADD 
    CONSTRAINT pk_dycc PRIMARY KEY (declared_yield_contract_commodity_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_commodity ADD 
    CONSTRAINT uk_dycc UNIQUE (declared_yield_contract_guid, crop_commodity_id, is_pedigree_ind) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_commodity ADD CONSTRAINT fk_dycc_dyc 
    FOREIGN KEY (declared_yield_contract_guid)
    REFERENCES cuws.declared_yield_contract(declared_yield_contract_guid)
;

ALTER TABLE cuws.declared_yield_contract_commodity ADD CONSTRAINT fk_dycc_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;

ALTER TABLE cuws.declared_yield_contract_commodity ADD CONSTRAINT fk_dycc_gmtc 
    FOREIGN KEY (grade_modifier_type_code)
    REFERENCES cuws.grade_modifier_type_code(grade_modifier_type_code)
;


