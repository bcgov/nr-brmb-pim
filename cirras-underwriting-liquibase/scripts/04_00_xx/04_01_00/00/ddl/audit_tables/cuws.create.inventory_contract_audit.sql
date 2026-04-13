CREATE TABLE cuws.inventory_contract_audit(
    inventory_contract_audit_id          numeric(9, 0)    NOT NULL,
    audit_transaction_type_code          varchar(10)      NOT NULL,
    audit_time_stamp                     timestamp(6)     NOT NULL,
    inventory_contract_guid              varchar(32)      NOT NULL,
    contract_id                          numeric(9, 0),
    crop_year                            numeric(4, 0),
    unseeded_intentions_submitted_ind    varchar(1),
    seeded_crop_report_submitted_ind     varchar(1),
    fertilizer_ind                       varchar(1),
    herbicide_ind                        varchar(1),
    tilliage_ind                         varchar(1),
    other_changes_ind                    varchar(1),
    other_changes_comment                varchar(256),
    grain_from_prev_year_ind             varchar(1),
    inv_update_timestamp                 timestamp(6),
    inv_update_user                      varchar(64),
    create_user                          varchar(64)      NOT NULL,
    create_date                          timestamp(6)     NOT NULL,
    update_user                          varchar(64)      NOT NULL,
    update_date                          timestamp(6)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_contract_audit.inventory_contract_audit_id IS 'Inventory Contract Audit Id is the ID of the Inventory Contract Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id (also in POLICY.CONTRACT_ID)'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.crop_year IS 'Crop Year is the year the grower is bound by the specified contract from cirr_grower_contract_years.crop_year'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.unseeded_intentions_submitted_ind IS 'Unseeded Intentions Submitted Ind indicates whether the the seeding intentions crop report was submitted'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.seeded_crop_report_submitted_ind IS 'Seeded Crop Report Ind indicates whether the seeded crop report was submitted'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.fertilizer_ind IS 'Fertilizer Ind shows whether fertilizer was used in the farm'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.herbicide_ind IS 'Herbicide Ind indicates whether herbicide was used on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.tilliage_ind IS 'Tilliage Ind indicate whether tilliage was used on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.other_changes_ind IS 'Other Changes Ind indicate whether other changes were made on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.other_changes_comment IS 'Other Changes Comment is comment provided for other changes made on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.grain_from_prev_year_ind IS 'Grain From Prev Year Ind indicate if grain was stored from previous year'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.inv_update_timestamp IS 'Inv Update Timestamp is the last time any inventory data was changed by the user.'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.inv_update_user IS 'Inv Update User is the last user that changed any inventory data.'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_contract_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_contract_audit IS 'Inventory Contract Audit is the audit table for inventory_contract'
;

ALTER TABLE cuws.inventory_contract_audit ADD 
    CONSTRAINT pk_icoa PRIMARY KEY (inventory_contract_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract_audit ADD CONSTRAINT fk_icoa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


