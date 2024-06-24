


CREATE TABLE cuws.risk_area(
    risk_area_id         numeric(10, 0)    NOT NULL,
    insurance_plan_id    numeric(9, 0)     NOT NULL,
    risk_area_name       varchar(100)      NOT NULL,
    description          varchar(500),
    effective_date       date              NOT NULL,
    expiry_date          date              NOT NULL,
    create_user          varchar(64)       NOT NULL,
    create_date          timestamp(0)      NOT NULL,
    update_user          varchar(64)       NOT NULL,
    update_date          timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.risk_area.risk_area_id IS 'Risk Area Id is a unique identifier for a RISK AREA generated from a surrogate key'
;
COMMENT ON COLUMN cuws.risk_area.insurance_plan_id IS 'Insurance Plan Id is a unique identifier for an insurance Plan from INSURANCE_PLAN'
;
COMMENT ON COLUMN cuws.risk_area.risk_area_name IS 'Risk Area Name is a human readable name for a risk area'
;
COMMENT ON COLUMN cuws.risk_area.description IS 'Description is a short description of the risk area'
;
COMMENT ON COLUMN cuws.risk_area.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.risk_area.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.risk_area.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.risk_area.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.risk_area.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.risk_area.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.risk_area IS 'The table defines Risk Areas by Plan.'
;

CREATE INDEX ix_ra_ip ON cuws.risk_area(insurance_plan_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.risk_area ADD 
    CONSTRAINT pk_ra PRIMARY KEY (risk_area_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.risk_area ADD 
    CONSTRAINT uk_ra UNIQUE (insurance_plan_id, risk_area_name) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.risk_area ADD CONSTRAINT fk_ra_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;
