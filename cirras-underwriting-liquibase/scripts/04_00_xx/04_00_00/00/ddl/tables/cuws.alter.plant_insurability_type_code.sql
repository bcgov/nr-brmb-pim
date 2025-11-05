ALTER TABLE cuws.plant_insurability_type_code ADD COLUMN insurance_plan_id numeric(9, 0) DEFAULT 5 NOT NULL;

ALTER TABLE cuws.plant_insurability_type_code ALTER COLUMN insurance_plan_id DROP DEFAULT;

COMMENT ON COLUMN cuws.plant_insurability_type_code.insurance_plan_id IS 'Insurance Plan Id is a unique Id of an insurance plan from CIRR_INSURANCE_PLANS.IP_ID'
;

CREATE INDEX ix_pitc_ip ON cuws.plant_insurability_type_code(insurance_plan_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.plant_insurability_type_code ADD CONSTRAINT fk_pitc_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;
