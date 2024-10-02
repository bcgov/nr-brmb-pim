CREATE TABLE cuws.grower_contract_year(
    grower_contract_year_id    numeric(10, 0)    NOT NULL,
    grower_id                  numeric(9, 0),
    insurance_plan_id          numeric(9, 0)     NOT NULL,
    contract_id                numeric(9, 0)     NOT NULL,
    crop_year                  numeric(4, 0)     NOT NULL,
    data_sync_trans_date       timestamp(0)      NOT NULL,
    create_user                varchar(64)       NOT NULL,
    create_date                timestamp(0)      NOT NULL,
    update_user                varchar(64)       NOT NULL,
    update_date                timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.grower_contract_year.grower_contract_year_id IS 'Grower Contract Year Id is a unique record identifier for Grower Contract Year records from cirr_grower_contract_years.grower_contract_year_id'
;
COMMENT ON COLUMN cuws.grower_contract_year.grower_id IS 'Grower Id is a unique key of a grower from CIRR_INSURED_GROWERS.IG_ID'
;
COMMENT ON COLUMN cuws.grower_contract_year.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.grower_contract_year.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id (also in POLICY.CONTRACT_ID)'
;
COMMENT ON COLUMN cuws.grower_contract_year.crop_year IS 'Crop Year is the year the grower is bound by the specified contract from cirr_grower_contract_years.crop_year'
;
COMMENT ON COLUMN cuws.grower_contract_year.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.grower_contract_year.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.grower_contract_year.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.grower_contract_year.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.grower_contract_year.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.grower_contract_year IS 'The table contains all grower contract year records from cirr_grower_contract_years'
;

CREATE INDEX ix_gcy_grw ON cuws.grower_contract_year(grower_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_gcy_ip ON cuws.grower_contract_year(insurance_plan_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.grower_contract_year ADD 
    CONSTRAINT pk_gcy PRIMARY KEY (grower_contract_year_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.grower_contract_year ADD 
    CONSTRAINT uk_gcy UNIQUE (contract_id, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.grower_contract_year ADD CONSTRAINT fk_gcy_grw 
    FOREIGN KEY (grower_id)
    REFERENCES cuws.grower(grower_id)
;

ALTER TABLE cuws.grower_contract_year ADD CONSTRAINT fk_gcy_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;


