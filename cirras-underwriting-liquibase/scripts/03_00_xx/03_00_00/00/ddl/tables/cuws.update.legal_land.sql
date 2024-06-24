

--Drop COLUMNS
ALTER TABLE cuws.legal_land DROP COLUMN data_sync_trans_date;


-- Add new total_acres column.
ALTER TABLE cuws.legal_land ADD COLUMN total_acres numeric(10, 4);

COMMENT ON COLUMN cuws.legal_land.total_acres IS 'Total Acres is the size of the legal land in acres.'
;



-- Add new primary_land_identifier_type_code column.
ALTER TABLE cuws.legal_land ADD COLUMN primary_land_identifier_type_code varchar(10);

COMMENT ON COLUMN cuws.legal_land.primary_land_identifier_type_code IS 'Primary Land Identifier Type Code is the type of Primary Property Identifier: PID, PIN, IR, LINC or OTHER.'
;

CREATE INDEX ix_ll_litc ON cuws.legal_land(primary_land_identifier_type_code)
 TABLESPACE pg_default
;

ALTER TABLE cuws.legal_land ADD CONSTRAINT fk_ll_litc 
    FOREIGN KEY (primary_land_identifier_type_code)
    REFERENCES cuws.land_identifier_type_code(land_identifier_type_code)
;



-- Add foreign-key to new code table for primary_reference_type_code.
CREATE INDEX ix_ll_prtc ON cuws.legal_land(primary_reference_type_code)
 TABLESPACE pg_default
;

ALTER TABLE cuws.legal_land ADD CONSTRAINT fk_ll_prtc 
    FOREIGN KEY (primary_reference_type_code)
    REFERENCES cuws.primary_reference_type_code(primary_reference_type_code)
;
