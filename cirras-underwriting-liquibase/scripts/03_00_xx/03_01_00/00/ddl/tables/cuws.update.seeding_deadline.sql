


-- Add new columns
ALTER TABLE cuws.seeding_deadline ADD COLUMN full_coverage_deadline_date_default DATE;
ALTER TABLE cuws.seeding_deadline ADD COLUMN final_coverage_deadline_date_default DATE;

COMMENT ON COLUMN cuws.seeding_deadline.full_coverage_deadline_date_default IS 'Full Coverage Deadline Date Default is the full coverage date which is rolled over'
;
COMMENT ON COLUMN cuws.seeding_deadline.final_coverage_deadline_date_default IS 'Final Coverage Deadline Date Default is the final coverage date which is rolled over'
;

--Set default values
UPDATE seeding_deadline set 
full_coverage_deadline_date_default = full_coverage_deadline_date,
final_coverage_deadline_date_default = final_coverage_deadline_date;


-- Make columns not nullable.
ALTER TABLE seeding_deadline ALTER COLUMN full_coverage_deadline_date_default SET NOT NULL;
ALTER TABLE seeding_deadline ALTER COLUMN final_coverage_deadline_date_default SET NOT NULL;

-- Add index to crop_year column
CREATE INDEX ix_sd_uy ON cuws.seeding_deadline(crop_year)
 TABLESPACE pg_default
;

-- Add foreign key to underwriting_year table
ALTER TABLE cuws.seeding_deadline ADD CONSTRAINT fk_sd_uy 
    FOREIGN KEY (crop_year)
    REFERENCES cuws.underwriting_year(crop_year)
;