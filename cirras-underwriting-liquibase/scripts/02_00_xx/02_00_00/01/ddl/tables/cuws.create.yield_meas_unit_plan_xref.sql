

CREATE TABLE cuws.yield_meas_unit_plan_xref(
    yield_meas_unit_plan_xref_guid    varchar(32)      NOT NULL,
    yield_meas_unit_type_code         varchar(10)      NOT NULL,
    insurance_plan_id                 numeric(9, 0)    NOT NULL,
    is_default_yield_unit_ind         varchar(1)       NOT NULL,
    create_user                       varchar(64)      NOT NULL,
    create_date                       timestamp(0)     NOT NULL,
    update_user                       varchar(64)      NOT NULL,
    update_date                       timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.yield_meas_unit_plan_xref_guid IS 'Yield Meas Unit Plan Xref Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.yield_meas_unit_type_code IS 'Yield Meas Unit Type Code is a unique record identifier for yield meas unit type records.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.insurance_plan_id IS 'Insurance Plan Id is a unique identifier for an insurance Plan from INSURANCE_PLAN'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.is_default_yield_unit_ind IS 'Is Default Yield Unit Ind is Y if YIELD_MEAS_UNIT_TYPE_CODE is the default unit for INSURANCE_PLAN_ID. Only one default can exist per plan.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.yield_meas_unit_plan_xref.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.yield_meas_unit_plan_xref IS 'The table defines valid yield units by insurance plan.'
;

CREATE INDEX ix_ymupx_ymutc ON cuws.yield_meas_unit_plan_xref(yield_meas_unit_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_ymupx_ip ON cuws.yield_meas_unit_plan_xref(insurance_plan_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.yield_meas_unit_plan_xref ADD 
    CONSTRAINT pk_ymupx PRIMARY KEY (yield_meas_unit_plan_xref_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.yield_meas_unit_plan_xref ADD 
    CONSTRAINT uk_ymupx UNIQUE (yield_meas_unit_type_code, insurance_plan_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.yield_meas_unit_plan_xref ADD CONSTRAINT fk_ymupx_ymutc 
    FOREIGN KEY (yield_meas_unit_type_code)
    REFERENCES cuws.yield_meas_unit_type_code(yield_meas_unit_type_code)
;

ALTER TABLE cuws.yield_meas_unit_plan_xref ADD CONSTRAINT fk_ymupx_ip 
    FOREIGN KEY (insurance_plan_id)
    REFERENCES cuws.insurance_plan(insurance_plan_id)
;


