
ALTER TABLE cuws.legal_land ADD COLUMN primary_property_identifier varchar(50);

COMMENT ON COLUMN cuws.legal_land.primary_property_identifier
    IS 'PRIMARY PROPERTY IDENTIFIER is a unique identifier assigned to a property that makes up part or all of a lot. The PROPERTY IDENTIFIER is a unique alpha-numeric assigned to a property outside of the CIRRAS system.';
    
	