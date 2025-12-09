-- 
-- TABLE: cuws.berries_2026_staging.
--

\qecho 'Create berries_2026_staging'

CREATE TABLE cuws.berries_2026_staging(
        input_line_number numeric(4) not null,
        contract_number numeric not null,
        crop_year numeric(4) not null,
        grower_contract_year_id numeric(10),
        contract_id numeric(9),
        property_identifier varchar,
        primary_property_identifier varchar(50),
        primary_land_identifier_type_code varchar(10),
        secondary_property_identifiers varchar(2000),
        legal_land_id numeric(10),
        bog_id varchar,
        field_bog_name varchar,
        field_id numeric(9),
        crop_name varchar not null,
        crop_commodity_id numeric(9),
        field_location varchar,
        field_order numeric(4),
        planting_number numeric(2),
        year_planted varchar,
        acres varchar,
        ownership varchar,
        variety_name varchar,
        crop_variety_id numeric(9),
        row_spacing varchar,
        plant_spacing varchar,
        bogs_mowed varchar,
        bogs_renovated varchar,
        harvested_ind varchar,
        is_quantity_insurable_ind varchar(1),
        is_plant_insurable_ind varchar(1),
        plant_insurability_type_code varchar(10),
        do_insert_legal_land_ind varchar(1)
) TABLESPACE pg_default;
