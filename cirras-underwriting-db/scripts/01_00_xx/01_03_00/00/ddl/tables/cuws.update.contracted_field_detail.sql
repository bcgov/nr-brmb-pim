


DROP INDEX IF EXISTS ix_cfd_cco;
ALTER TABLE cuws.contracted_field_detail DROP CONSTRAINT fk_cfd_cco;
ALTER TABLE cuws.contracted_field_detail DROP column underseeded_to_crop_commodity_id;
ALTER TABLE cuws.contracted_field_detail DROP column underseeded_acres;

