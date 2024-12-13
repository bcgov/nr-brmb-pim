ALTER TABLE cuws.underwriting_comment ADD COLUMN verified_yield_summary_guid varchar(32);

COMMENT ON COLUMN cuws.underwriting_comment.verified_yield_summary_guid IS 'Verified Yield Summary GUID is the primary key used to identify the record'
;

CREATE INDEX ix_uc_vys ON cuws.underwriting_comment(verified_yield_summary_guid)
 TABLESPACE pg_default
;

ALTER TABLE cuws.underwriting_comment ADD CONSTRAINT fk_uc_vys 
    FOREIGN KEY (verified_yield_summary_guid)
    REFERENCES cuws.verified_yield_summary(verified_yield_summary_guid)
;