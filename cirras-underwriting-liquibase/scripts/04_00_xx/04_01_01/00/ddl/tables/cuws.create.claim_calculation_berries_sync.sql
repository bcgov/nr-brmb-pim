CREATE TABLE cuws.claim_calculation_berries_sync(
    claim_calculation_berries_sync_guid    varchar(32)       NOT NULL,
    crop_commodity_id                      numeric(9, 0)     NOT NULL,
    contract_id                            numeric(9, 0)     NOT NULL,
    crop_year                              numeric(4, 0)     NOT NULL,
    claim_calculation_guid                 varchar(32)       NOT NULL,
    claim_calculation_berries_guid         varchar(32)       NOT NULL,
    total_yield_for_calculation            numeric(14, 4),
    calculation_status_code                varchar(16)       NOT NULL,
    calculation_version                    numeric(2, 0)     NOT NULL,
    data_sync_trans_date                   timestamp(0)      NOT NULL,
    create_user                            varchar(64)       NOT NULL,
    create_date                            timestamp(0)      NOT NULL,
    update_user                            varchar(64)       NOT NULL,
    update_date                            timestamp(0)      NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.claim_calculation_berries_sync.claim_calculation_berries_sync_guid IS 'Claim Calculation Berries Sync Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id '
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.crop_year IS 'Crop Year is the year of the Claim'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.claim_calculation_guid IS 'Claim Calculation Guid is a unique key of a claims calculation'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.claim_calculation_berries_guid IS 'Claim Calculation Berries Guid is a unique key of a claims calculation berries record'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.total_yield_for_calculation IS 'Total Yield For Calculation is the total yield for the claim calculation. Calculated from TOTAL_YIELD_FROM_DOP/ADJUSTER + YIELD_ASSESSMENT'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.calculation_status_code IS 'Calculation Status is the status of the claim calculation'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.calculation_version IS 'Calculation Version is the version number of the calculation of a claim'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.claim_calculation_berries_sync.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.claim_calculation_berries_sync IS 'Claim Calculation Berries Sync contains cached berries data from the claims calculator app'
;

CREATE INDEX ix_ccbs_cco ON cuws.claim_calculation_berries_sync(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.claim_calculation_berries_sync ADD 
    CONSTRAINT pk_ccbs PRIMARY KEY (claim_calculation_berries_sync_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.claim_calculation_berries_sync ADD 
    CONSTRAINT uk_ccbs UNIQUE (crop_commodity_id, contract_id, crop_year, calculation_version) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.claim_calculation_berries_sync ADD 
    CONSTRAINT uk_ccbs_ccb UNIQUE (claim_calculation_berries_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.claim_calculation_berries_sync ADD CONSTRAINT fk_ccbs_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


