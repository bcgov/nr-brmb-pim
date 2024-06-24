
-- 
-- TABLE: cuws.policy 
--

CREATE TABLE cuws.policy(
    policy_id               numeric(9, 0)    NOT NULL,
    grower_id               numeric(9, 0)    NOT NULL,
    insurance_plan_id       numeric(9, 0)    NOT NULL,
    policy_status_code      varchar(16)      NOT NULL,
    office_id               numeric(9, 0)    NOT NULL,
    policy_number           varchar(20)      NOT NULL,
    contract_number         varchar(16)      NOT NULL,
    contract_id             numeric(9, 0)    NOT NULL,
    crop_year               numeric(4, 0)    NOT NULL,
    data_sync_trans_date    timestamp        NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp        NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp        NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.policy.policy_id IS 'Policy Id is a unique key of a policy from cirr_insrnc_prdct_prchses.ipl_ipl_id '
;
COMMENT ON COLUMN cuws.policy.grower_id IS 'Grower Id is a unique key of a grower from CIRR_INSURED_GROWERS.IG_ID'
;
COMMENT ON COLUMN cuws.policy.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.policy.policy_status_code IS 'Policy Status Code is the status code of the policy from CIRR_POLICY_STATUS_TYPES.POLICY_STATUS_TYPE_CODE'
;
COMMENT ON COLUMN cuws.policy.office_id IS 'Office Id is a unique Id of an office from CIRR_OFFICES.OFF_ID'
;
COMMENT ON COLUMN cuws.policy.policy_number IS 'Policy Number is the number of the policy from CIRR_CONTRACT_NUMBERS.CONTRACT_NUMBER-CIRR_INSURANCE_POLICIES.CROP_YEAR'
;
COMMENT ON COLUMN cuws.policy.contract_number IS 'Contract Number is the number of the contract from CIRR_CONTRACT_NUMBERS.CONTRACT_NUMBER'
;
COMMENT ON COLUMN cuws.policy.contract_id IS 'Contract Id is a unique key of a contract from cirr_contract_numbers.cn_id '
;
COMMENT ON COLUMN cuws.policy.crop_year IS 'Crop Year is the year of the policy from cirr_insurance_policies.crop_year'
;
COMMENT ON COLUMN cuws.policy.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.policy.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.policy.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.policy.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.policy.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.policy IS 'The table contains all policies from CIRR_INSURANCE_POLICIES'
;

ALTER TABLE cuws.policy ADD 
    CONSTRAINT pk_pl PRIMARY KEY (policy_id) USING INDEX TABLESPACE pg_default 
;

CREATE INDEX ix_pl_ip ON cuws.policy(insurance_plan_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_pl_psc ON cuws.policy(policy_status_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_pl_of ON cuws.policy(office_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_pl_grw ON cuws.policy(grower_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.policy ADD CONSTRAINT fk_pl_grw 
    FOREIGN KEY (grower_id)
    REFERENCES cuws.grower(grower_id)
;

ALTER TABLE cuws.policy ADD CONSTRAINT fk_pl_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;

ALTER TABLE cuws.policy ADD CONSTRAINT fk_pl_psc 
    FOREIGN KEY (policy_status_code)
    REFERENCES cuws.policy_status_code(policy_status_code)
;

ALTER TABLE cuws.policy ADD CONSTRAINT fk_pl_of 
    FOREIGN KEY (office_id)
    REFERENCES cuws.office(office_id)
;
