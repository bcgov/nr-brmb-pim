-- 
-- TABLE: cuws.planting_2024_staging.
--



CREATE TABLE cuws.planting_2024_staging(
	input_line_number numeric(4,0) not null,
	policy_number varchar not null,
	crop_year numeric(4,0),
	grower_contract_year_id numeric(10,0),
	contract_id numeric(9,0),
	field_order numeric(3,0),
	field_name varchar,
	field_id numeric(9,0),
	legal_location varchar,
	primary_property_identifier varchar not null,
	primary_land_identifier_type_code varchar(10),
	legal_land_id numeric(10,0),
	planting_number numeric(2,0),
	variety_name varchar,
	crop_variety_id numeric(9,0),
	crop_commodity_id numeric(9,0),
	commodity_type_code varchar(30),
	field_acres numeric(10,4) not null,
	seeding_year numeric(4,0) not null,
	is_irrigated_ind varchar,
	is_quantity_insurable_ind varchar,
	is_awp_eligible_ind varchar,
	risk_area_name varchar,
	risk_area_id numeric(10,0),
	do_insert_field_ind varchar(1),
	do_insert_legal_land_ind varchar(1)
) TABLESPACE pg_default;
