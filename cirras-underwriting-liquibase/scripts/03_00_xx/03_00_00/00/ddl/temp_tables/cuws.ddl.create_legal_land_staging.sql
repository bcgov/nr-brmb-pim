-- 
-- TABLE: cuws.legal_land_staging.
--



CREATE TABLE cuws.legal_land_staging(
	legal_land_id numeric(10,0) not null,
	primary_property_identifier varchar(50),
	primary_land_identifier_type_code varchar(10),
	total_acres numeric(10, 4)
) TABLESPACE pg_default;
