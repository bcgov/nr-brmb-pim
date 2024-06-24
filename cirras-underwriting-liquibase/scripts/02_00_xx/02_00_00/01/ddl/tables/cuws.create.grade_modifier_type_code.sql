

CREATE TABLE cuws.grade_modifier_type_code(
    grade_modifier_type_code    varchar(10)     NOT NULL,
    description                 varchar(100)    NOT NULL,
    effective_date              date            NOT NULL,
    expiry_date                 date            NOT NULL,
    create_user                 varchar(64)     NOT NULL,
    create_date                 timestamp(0)    NOT NULL,
    update_user                 varchar(64)     NOT NULL,
    update_date                 timestamp(0)    NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.grade_modifier_type_code.grade_modifier_type_code IS 'Grade Modifier Type Code is a unique record identifier for grade modifier type records.'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.description IS 'Description is a description of a grade modifier type code.'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.grade_modifier_type_code IS 'The table defines grades for yield.'
;

ALTER TABLE cuws.grade_modifier_type_code ADD 
    CONSTRAINT pk_gmtc PRIMARY KEY (grade_modifier_type_code) USING INDEX TABLESPACE pg_default 
;

