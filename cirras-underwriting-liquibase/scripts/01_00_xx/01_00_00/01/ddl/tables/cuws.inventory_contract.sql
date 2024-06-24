CREATE TABLE cuws.inventory_contract(
    inventory_contract_guid              varchar(32)      NOT NULL,
    contract_id                          numeric(9, 0)    NOT NULL,
    crop_year                            numeric(4, 0)    NOT NULL,
    unseeded_intentions_submitted_ind    varchar(1)       NOT NULL,
    seeded_crop_report_submitted_ind     varchar(1)       NOT NULL,
    fertilizer_ind                       varchar(1)       NOT NULL,
    herbicide_ind                        varchar(1)       NOT NULL,
    tilliage_ind                         varchar(1)       NOT NULL,
    other_changes_ind                    varchar(1)       NOT NULL,
    other_changes_comment                varchar(256),
    grain_from_prev_year_ind             varchar(1)       NOT NULL,
    create_user                          varchar(64)      NOT NULL,
    create_date                          timestamp(0)     NOT NULL,
    update_user                          varchar(64)      NOT NULL,
    update_date                          timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_contract.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id (also in POLICY.CONTRACT_ID)'
;
COMMENT ON COLUMN cuws.inventory_contract.crop_year IS 'Crop Year is the year the grower is bound by the specified contract from cirr_grower_contract_years.crop_year'
;
COMMENT ON COLUMN cuws.inventory_contract.unseeded_intentions_submitted_ind IS 'Unseeded Intentions Submitted Ind indicates whether the the seeding intentions crop report was submitted'
;
COMMENT ON COLUMN cuws.inventory_contract.seeded_crop_report_submitted_ind IS 'Seeded Crop Report Ind indicates whether the seeded crop report was submitted'
;
COMMENT ON COLUMN cuws.inventory_contract.fertilizer_ind IS 'Fertilizer Ind shows whether fertilizer was used in the farm'
;
COMMENT ON COLUMN cuws.inventory_contract.herbicide_ind IS 'Herbicide Ind indicates whether herbicide was used on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract.tilliage_ind IS 'Tilliage Ind indicate whether tilliage was used on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract.other_changes_ind IS 'Other Changes Ind indicate whether other changes were made on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract.other_changes_comment IS 'Other Changes Comment is comment provided for other changes made on the farm'
;
COMMENT ON COLUMN cuws.inventory_contract.grain_from_prev_year_ind IS 'Grain From Prev Year Ind indicate if grain was stored from previous year'
;
COMMENT ON COLUMN cuws.inventory_contract.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_contract.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_contract.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_contract.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_contract IS 'The table contains general inventory data for a contract'
;

ALTER TABLE cuws.inventory_contract ADD 
    CONSTRAINT pk_ico PRIMARY KEY (inventory_contract_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract ADD 
    CONSTRAINT uk_ico UNIQUE (contract_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract ADD CONSTRAINT fk_ico_gcy 
    FOREIGN KEY (contract_id, crop_year)
    REFERENCES cuws.grower_contract_year(contract_id, crop_year)
;


