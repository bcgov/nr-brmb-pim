-- 
-- TABLE: cuws.planting_2022_staging.
--



CREATE TABLE cuws.planting_2022_staging(
	input_line_number numeric(4,0) not null,
	policy_number varchar,
	crop_year varchar,
	field_order varchar,
	field_name varchar,
	legal_location varchar,
	field_id varchar,
	legal_description varchar,
	legal_land varchar,
	crop_seeded varchar,
	variety varchar,
	acres_seeded varchar,
	seeding_date varchar,
	underseeded varchar,
	office varchar,
	same_field_as_prev varchar
) TABLESPACE pg_default;
