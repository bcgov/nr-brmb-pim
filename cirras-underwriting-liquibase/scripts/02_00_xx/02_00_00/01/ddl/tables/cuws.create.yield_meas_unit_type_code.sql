

CREATE TABLE cuws.yield_meas_unit_type_code(
    yield_meas_unit_type_code    varchar(10)      NOT NULL,
    description                  varchar(100)     NOT NULL,
    decimal_precision            numeric(2, 0)    NOT NULL,
    effective_date               date             NOT NULL,
    expiry_date                  date             NOT NULL,
    create_user                  varchar(64)      NOT NULL,
    create_date                  timestamp(0)     NOT NULL,
    update_user                  varchar(64)      NOT NULL,
    update_date                  timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.yield_meas_unit_type_code.yield_meas_unit_type_code IS 'Yield Meas Unit Type Code is a unique record identifier for yield meas unit type records.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.description IS 'Description is a description of a yield meas unit type code.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.decimal_precision IS 'Decimal Precision is the number of decimals shown on the yield screen for this unit.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.yield_meas_unit_type_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.yield_meas_unit_type_code IS 'The table defines measurement units for yield.'
;

ALTER TABLE cuws.yield_meas_unit_type_code ADD 
    CONSTRAINT pk_ymutc PRIMARY KEY (yield_meas_unit_type_code) USING INDEX TABLESPACE pg_default 
;

