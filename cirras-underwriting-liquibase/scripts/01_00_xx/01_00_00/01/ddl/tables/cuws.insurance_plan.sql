-- 
-- TABLE: cuws.insurance_plan 
--

CREATE TABLE cuws.insurance_plan(
    insurance_plan_id       numeric(9, 0)    NOT NULL,
    insurance_plan_name     varchar(50)      NOT NULL,
    effective_date          date             NOT NULL,
    expiry_date             date             NOT NULL,
    data_sync_trans_date    timestamp(0)     NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp(0)     NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.insurance_plan.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;
COMMENT ON COLUMN cuws.insurance_plan.insurance_plan_name IS 'Insurance Plan Name is the name of the insurance plan from CIRR_INSURANCE_PLANS.PLAN_NAME'
;
COMMENT ON COLUMN cuws.insurance_plan.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.insurance_plan.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.insurance_plan.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.insurance_plan.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.insurance_plan.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.insurance_plan.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.insurance_plan.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.insurance_plan IS 'The table contains all insurance plans from CIRR_INSURANCE_PLANS (except unused UCV record)'
;

-- 
-- TABLE: cuws.insurance_plan 
--

ALTER TABLE cuws.insurance_plan ADD 
    CONSTRAINT pk_ip PRIMARY KEY (insurance_plan_id) USING INDEX TABLESPACE pg_default 
;

