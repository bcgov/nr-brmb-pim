

CREATE TABLE cuws.grade_modifier(
    grade_modifier_guid         varchar(32)       NOT NULL,
    crop_commodity_id           numeric(9, 0)     NOT NULL,
    crop_year                   numeric(4, 0)     NOT NULL,
    grade_modifier_type_code    varchar(10)       NOT NULL,
    grade_modifier_value        numeric(14, 4)    NOT NULL,
    create_user                 varchar(64)       NOT NULL,
    create_date                 timestamp(0)      NOT NULL,
    update_user                 varchar(64)       NOT NULL,
    update_date                 timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.grade_modifier.grade_modifier_guid IS 'Grade Modifier Guid is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.grade_modifier.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.grade_modifier.crop_year IS 'Crop Year is the year for which the Grade Modifier Value applies.'
;
COMMENT ON COLUMN cuws.grade_modifier.grade_modifier_type_code IS 'Grade Modifier Type Code is the grade for which the Grade Modifier Value is defined.'
;
COMMENT ON COLUMN cuws.grade_modifier.grade_modifier_value IS 'Grade Modifier Value is the value for this Grade Modifier Type Code for this Year and Commodity.'
;
COMMENT ON COLUMN cuws.grade_modifier.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.grade_modifier.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.grade_modifier.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.grade_modifier.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.grade_modifier IS 'The table defines grade modifiers by year and commodity.'
;

CREATE INDEX ix_gm_gmtc ON cuws.grade_modifier(grade_modifier_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_gm_cco ON cuws.grade_modifier(crop_commodity_id)
 TABLESPACE pg_default
;
ALTER TABLE cuws.grade_modifier ADD 
    CONSTRAINT pk_gm PRIMARY KEY (grade_modifier_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.grade_modifier ADD 
    CONSTRAINT uk_gm UNIQUE (crop_commodity_id, crop_year, grade_modifier_type_code) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.grade_modifier ADD CONSTRAINT fk_gm_gmtc 
    FOREIGN KEY (grade_modifier_type_code)
    REFERENCES cuws.grade_modifier_type_code(grade_modifier_type_code)
;

ALTER TABLE cuws.grade_modifier ADD CONSTRAINT fk_gm_cco 
    FOREIGN KEY (crop_commodity_id)
    REFERENCES cuws.crop_commodity(crop_commodity_id)
;


