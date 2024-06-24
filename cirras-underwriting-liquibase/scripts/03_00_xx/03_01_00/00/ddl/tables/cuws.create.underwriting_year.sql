

CREATE TABLE cuws.underwriting_year(
    underwriting_year_guid    varchar(32)      NOT NULL,
    crop_year                 numeric(4, 0)    NOT NULL,
    create_user               varchar(64)      NOT NULL,
    create_date               timestamp(0)     NOT NULL,
    update_user               varchar(64)      NOT NULL,
    update_date               timestamp(0)     NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.underwriting_year.underwriting_year_guid IS 'Underwriting Year GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.underwriting_year.crop_year IS 'Crop Year is the underwriting year of the data'
;
COMMENT ON COLUMN cuws.underwriting_year.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.underwriting_year.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.underwriting_year.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.underwriting_year.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.underwriting_year IS 'The table contains general underwriting information'
;

CREATE UNIQUE INDEX uk_uy_cy ON cuws.underwriting_year(crop_year)
 TABLESPACE pg_default
;
ALTER TABLE cuws.underwriting_year ADD 
    CONSTRAINT pk_uy PRIMARY KEY (underwriting_year_guid) USING INDEX TABLESPACE pg_default 
;

--Insert data for each seeded deadline and grade modifier crop year
INSERT INTO cuws.underwriting_year(underwriting_year_guid, crop_year, create_user, create_date, update_user, update_date)
SELECT replace(cast(gen_random_uuid() as text), '-', ''), 
		crop_year, 
		'CUWS_03_01_00', 
		now(), 
		'CUWS_03_01_00', 
		now()
FROM
(SELECT distinct crop_year
FROM seeding_deadline
UNION
SELECT distinct crop_year
FROM grade_modifier) as t
ON CONFLICT DO NOTHING;

