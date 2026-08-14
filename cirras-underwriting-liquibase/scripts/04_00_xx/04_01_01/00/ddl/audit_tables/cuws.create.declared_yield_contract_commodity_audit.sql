CREATE TABLE cuws.declared_yield_contract_commodity_audit(
    declared_yield_contract_commodity_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                   varchar(10)       NOT NULL,
    audit_time_stamp                              timestamp(6)      NOT NULL,
    declared_yield_contract_commodity_guid        varchar(32)       NOT NULL,
    declared_yield_contract_guid                  varchar(32),
    crop_commodity_id                             numeric(9, 0),
    is_pedigree_ind                               varchar(1),
    harvested_acres                               numeric(14, 4),
    stored_yield                                  numeric(14, 4),
    stored_yield_default_unit                     numeric(14, 4),
    sold_yield                                    numeric(14, 4),
    sold_yield_default_unit                       numeric(14, 4),
    grade_modifier_type_code                      varchar(10),
    create_user                                   varchar(64)       NOT NULL,
    create_date                                   timestamp(0)      NOT NULL,
    update_user                                   varchar(64)       NOT NULL,
    update_date                                   timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.declared_yield_contract_commodity_audit_id IS 'Declared Yield Contract Commodity Audit Id is the ID of the Declared Yield Contract Commodity Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.declared_yield_contract_commodity_guid IS 'Declared Yield Contract Commodity Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.declared_yield_contract_guid IS 'Declared Yield Contract Guid links to a record in DECLARED_YIELD_CONTRACT table'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the yield is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.harvested_acres IS 'Harvested Acres is the Harvested Acres for the Commodity from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.stored_yield IS 'Stored Yield is the yield that is stored on farm for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.ENTERED_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.stored_yield_default_unit IS 'Stored Yield is the yield that is stored on farm for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.sold_yield IS 'Sold Yield is the yield that is sold for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.ENTERED_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.sold_yield_default_unit IS 'Sold Yield is the yield that is sold for the Commodity from the Declaration of Production sheet, in DECLARED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.grade_modifier_type_code IS 'Grade Modifier Type Code is the grade of the yield from the Declaration of Production sheet.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_contract_commodity_audit IS 'Declared Yield Contract Commodity Audit is the audit table for declared_yield_contract_commodity'
;

ALTER TABLE cuws.declared_yield_contract_commodity_audit ADD 
    CONSTRAINT pk_dycca PRIMARY KEY (declared_yield_contract_commodity_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_commodity_audit ADD CONSTRAINT fk_dycca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


