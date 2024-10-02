


-- Add index to crop_year column
CREATE INDEX ix_gm_uy ON cuws.grade_modifier(crop_year)
 TABLESPACE pg_default
;

-- Add foreign key to underwriting_year table
ALTER TABLE cuws.grade_modifier ADD CONSTRAINT fk_gm_uy 
    FOREIGN KEY (crop_year)
    REFERENCES cuws.underwriting_year(crop_year)
;