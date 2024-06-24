
-- 
-- TABLE: cuws.office 
--

CREATE TABLE cuws.office(
    office_id               numeric(9, 0)    NOT NULL,
    office_name             varchar(50)      NOT NULL,
    data_sync_trans_date    timestamp        NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp        NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp        NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.office.office_id IS 'Office Id is a unique Id of an office from CIRR_OFFICES.OFF_ID'
;
COMMENT ON COLUMN cuws.office.office_name IS 'Office Name is the name of the office from CIRR_OFFICES.OFFICE_NAME'
;
COMMENT ON COLUMN cuws.office.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.office.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.office.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.office.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.office.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.office IS 'The table contains all offices from CIRR_OFFICES'
;

ALTER TABLE cuws.office ADD 
    CONSTRAINT pk_of PRIMARY KEY (office_id) USING INDEX TABLESPACE pg_default
;
