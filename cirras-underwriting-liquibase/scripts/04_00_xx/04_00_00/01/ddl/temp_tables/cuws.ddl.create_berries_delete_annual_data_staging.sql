-- 
-- TABLE: cuws.berries_delete_annual_data_staging.
--

\qecho 'Create berries_delete_annual_data_staging table'
CREATE TABLE cuws.berries_delete_annual_data_staging(
    field_id numeric(9,0) NOT NULL,
    annual_field_detail_id numeric(10,0) NOT NULL,
    contracted_field_detail_id numeric(10,0) NOT NULL    
) TABLESPACE pg_default;

