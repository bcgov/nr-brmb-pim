


--Drop old column
ALTER TABLE cuws.underwriting_comment DROP column inventory_field_guid;

ALTER TABLE cuws.underwriting_comment
ADD COLUMN annual_field_detail_id numeric(10, 0) NOT NULL;

COMMENT ON COLUMN cuws.underwriting_comment.annual_field_detail_id IS 'Annual Field Detail Id is a unique key of a lot from cirr_annual_lot_detail.annual_lot_detail_id'
;

CREATE INDEX ix_uc_afd ON cuws.underwriting_comment(annual_field_detail_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.underwriting_comment ADD CONSTRAINT fk_uc_afd 
    FOREIGN KEY (annual_field_detail_id)
    REFERENCES cuws.annual_field_detail(annual_field_detail_id)
;
