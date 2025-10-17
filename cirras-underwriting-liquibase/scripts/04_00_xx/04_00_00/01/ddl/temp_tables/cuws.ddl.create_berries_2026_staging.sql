-- 
-- TABLE: cuws.berries_2026_staging.
--

\qecho 'Create berries_2026_staging'

CREATE TABLE cuws.berries_2026_staging(
	input_line_number numeric(4,0) not null,
	contract_number numeric not null,
	crop_year numeric(4,0) not null,
	property_identifier varchar,
	bog_id varchar,
	field_bog_name varchar,
	crop_name varchar not null,
	field_location varchar,
	year_planted varchar,
	acres varchar,
	ownership varchar,
	variety_name varchar,
	row_spacing varchar,
	plant_spacing varchar,
	bogs_mowed varchar,
	bogs_renovated varchar,
	harvested_ind varchar
) TABLESPACE pg_default;
