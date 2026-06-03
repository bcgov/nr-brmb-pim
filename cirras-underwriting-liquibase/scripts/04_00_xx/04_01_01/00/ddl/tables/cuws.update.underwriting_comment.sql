--Alter COLUMNS
ALTER TABLE cuws.underwriting_comment ADD COLUMN is_forced_ind varchar(1);

UPDATE underwriting_comment SET is_forced_ind = 'N';

ALTER TABLE cuws.underwriting_comment ALTER COLUMN is_forced_ind SET NOT NULL;

COMMENT ON COLUMN cuws.underwriting_comment.is_forced_ind IS 'Is Forced Ind denotes whether the comment was forced (Y) or no (N)'
;
