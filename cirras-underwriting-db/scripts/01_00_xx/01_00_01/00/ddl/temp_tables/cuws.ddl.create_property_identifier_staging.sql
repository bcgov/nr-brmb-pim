-- 
-- TABLE: cuws.property_identifier_staging.
--



CREATE TABLE cuws.property_identifier_staging(
	legal_land_id numeric(10,0) not null,
	primary_property_identifier varchar(50) not null
) TABLESPACE pg_default;
