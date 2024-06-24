-- 
-- TABLE: cuws.policy_status_code 
--

CREATE TABLE cuws.policy_status_code(
    policy_status_code      varchar(16)     NOT NULL,
    description             varchar(100)    NOT NULL,
    effective_date          date            NOT NULL,
    expiry_date             date            NOT NULL,
    data_sync_trans_date    timestamp(0)    NOT NULL,
    create_user             varchar(64)     NOT NULL,
    create_date             timestamp(0)    NOT NULL,
    update_user             varchar(64)     NOT NULL,
    update_date             timestamp(0)    NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.policy_status_code.policy_status_code IS 'Policy Status Code is the status code of the policy from CIRR_POLICY_STATUS_TYPES.POLICY_STATUS_TYPE_CODE'
;
COMMENT ON COLUMN cuws.policy_status_code.description IS 'Description is the name of the status from CIRR_POLICY_STATUS_TYPES.DESCRIPTION'
;
COMMENT ON COLUMN cuws.policy_status_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.policy_status_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.policy_status_code.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.policy_status_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.policy_status_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.policy_status_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.policy_status_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.policy_status_code IS 'The table contains all claim status codes from CIRR_POLICY_STATUS_TYPES'
;

-- 
-- TABLE: cuws.policy_status_code 
--

ALTER TABLE cuws.policy_status_code ADD 
    CONSTRAINT pk_psc PRIMARY KEY (policy_status_code) USING INDEX TABLESPACE pg_default 
;

