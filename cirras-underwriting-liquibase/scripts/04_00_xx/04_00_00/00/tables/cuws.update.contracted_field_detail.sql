ALTER TABLE cuws.contracted_field_detail
ADD is_leased_ind VARCHAR(1);

UPDATE cuws.contracted_field_detail
SET is_leased_ind = 'N';

ALTER TABLE cuws.contracted_field_detail
ALTER COLUMN is_leased_ind SET NOT NULL;

COMMENT ON COLUMN cuws.contracted_field_detail.is_leased_ind
    IS 'Is Leased Ind denotes whether the field is Leased (Y) or owned (N)';