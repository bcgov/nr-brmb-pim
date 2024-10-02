-- 
-- TABLE: cuws.grower 
--

CREATE TABLE cuws.grower(
    grower_id               numeric(9, 0)     NOT NULL,
    grower_number           numeric(10, 0)    NOT NULL,
    grower_name             varchar(100)      NOT NULL,
    grower_address_line1    varchar(200),
    grower_address_line2    varchar(200),
    grower_postal_code      varchar(10),
    grower_city             varchar(40),
    city_id                 numeric(9, 0),
    grower_province         varchar(2),
    data_sync_trans_date    timestamp(0)      NOT NULL,
    create_user             varchar(64)       NOT NULL,
    create_date             timestamp(0)      NOT NULL,
    update_user             varchar(64)       NOT NULL,
    update_date             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.grower.grower_id IS 'Grower Id is a unique key of a grower from CIRR_INSURED_GROWERS.IG_ID'
;
COMMENT ON COLUMN cuws.grower.grower_number IS 'Grower Number from CIRR_INSURED_GROWERS.GROWER_NUMBER'
;
COMMENT ON COLUMN cuws.grower.grower_name IS 'Grower Name from CIRR_INSURED_GROWERS.GROWER_LEGAL_NAME'
;
COMMENT ON COLUMN cuws.grower.grower_address_line1 IS 'Grower Address Line 1 from CIRR_INSURED_GROWERS.LEGAL_ADDRESS_LINE1'
;
COMMENT ON COLUMN cuws.grower.grower_address_line2 IS 'Grower Address Line 2 from CIRR_INSURED_GROWERS.LEGAL_ADDRESS_LINE2'
;
COMMENT ON COLUMN cuws.grower.grower_postal_code IS 'Grower Postal Code from CIRR_INSURED_GROWERS.LEGAL_POSTAL_CODE'
;
COMMENT ON COLUMN cuws.grower.grower_city IS 'Grower City from CIRR_INSURED_GROWERS.CITY_CITY_ID (CIRR_CITIES.NAME)'
;
COMMENT ON COLUMN cuws.grower.city_id IS 'City Id is a unique key of a city from CIRR_INSURED_GROWERS.CITY_CITY_ID'
;
COMMENT ON COLUMN cuws.grower.grower_province IS 'Grower Province is the abbreviation of Province or State from CIRR_PROVINCES.CODE'
;
COMMENT ON COLUMN cuws.grower.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.grower.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.grower.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.grower.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.grower.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.grower IS 'The table contains all growers from cirr_insured_growers'
;

-- 
-- TABLE: cuws.grower 
--

ALTER TABLE cuws.grower ADD 
    CONSTRAINT pk_gwr PRIMARY KEY (grower_id) USING INDEX TABLESPACE pg_default 
;

