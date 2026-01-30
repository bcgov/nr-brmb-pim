ALTER TABLE cuws.field
ADD location VARCHAR(128);

COMMENT ON COLUMN cuws.field.location
    IS 'Location is the description of the location of the field.';

