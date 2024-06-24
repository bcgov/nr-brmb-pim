


-- Add new columns
ALTER TABLE cuws.grade_modifier_type_code ADD COLUMN effective_year numeric(4, 0);
ALTER TABLE cuws.grade_modifier_type_code ADD COLUMN expiry_year numeric(4, 0);

COMMENT ON COLUMN cuws.grade_modifier_type_code.effective_year IS 'Effective Year is the year when the record was first active'
;
COMMENT ON COLUMN cuws.grade_modifier_type_code.expiry_year IS 'Expiry Year is the year when the record was last active'
;

-- Insert default data
UPDATE grade_modifier_type_code set 
effective_year = DATE_PART('Year', effective_date),
expiry_year = DATE_PART('Year', expiry_date);

-- Make column nullable
ALTER TABLE grade_modifier_type_code ALTER COLUMN effective_year SET NOT NULL;

-- Drop columns
ALTER TABLE cuws.grade_modifier_type_code DROP COLUMN effective_date;
ALTER TABLE cuws.grade_modifier_type_code DROP COLUMN expiry_date;
