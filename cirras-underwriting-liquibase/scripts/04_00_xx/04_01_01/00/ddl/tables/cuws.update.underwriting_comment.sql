--Alter COLUMNS
ALTER TABLE cuws.underwriting_comment ADD COLUMN is_forced_ind varchar(1) NULL;

COMMENT ON COLUMN cuws.underwriting_comment.is_forced_ind IS 'Is Forced Ind denotes whether the comment was forced (Y) or no (N)'
;