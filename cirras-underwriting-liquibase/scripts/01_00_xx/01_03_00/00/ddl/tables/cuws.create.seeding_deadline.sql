CREATE TABLE cuws.seeding_deadline(
    seeding_deadline_guid           varchar(32)      NOT NULL,
    commodity_type_code             varchar(30)      NOT NULL,
    crop_year                       numeric(4, 0)    NOT NULL,
    full_coverage_deadline_date     date             NOT NULL,
    final_coverage_deadline_date    date             NOT NULL,
    create_user                     varchar(64)      NOT NULL,
    create_date                     timestamp(0)     NOT NULL,
    update_user                     varchar(64)      NOT NULL,
    update_date                     timestamp(0)     NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.seeding_deadline.seeding_deadline_guid IS 'Seeding Deadline Guid is the primary key used to identify a table record'
;
COMMENT ON COLUMN cuws.seeding_deadline.commodity_type_code IS 'Commodity Type Code is a unique value from cirr_commodity_type_code.commodity_type_code'
;
COMMENT ON COLUMN cuws.seeding_deadline.crop_year IS 'Crop Year is the year of the deadlines'
;
COMMENT ON COLUMN cuws.seeding_deadline.full_coverage_deadline_date IS 'Full Coverage Deadline Date is the last seeding date where the producer can get full coverage'
;
COMMENT ON COLUMN cuws.seeding_deadline.final_coverage_deadline_date IS 'Final Coverage Deadline Date is the last seeding date where the producer can get any coverage'
;
COMMENT ON COLUMN cuws.seeding_deadline.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.seeding_deadline.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.seeding_deadline.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.seeding_deadline.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.seeding_deadline IS 'Seeding Deadline stores the full coverage and final coverage deadlines for a commodity type and year'
;

CREATE INDEX ix_sd_ctc ON cuws.seeding_deadline(commodity_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.seeding_deadline ADD 
    CONSTRAINT pk_sd PRIMARY KEY (seeding_deadline_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.seeding_deadline ADD 
    CONSTRAINT uk_sd UNIQUE (commodity_type_code, crop_year) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.seeding_deadline ADD CONSTRAINT fk_sd_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES cuws.commodity_type_code(commodity_type_code)
;
