CREATE TABLE cuws.contact(
    contact_id              numeric(9, 0)    NOT NULL,
    first_name              varchar(50),
    last_name               varchar(50),
    data_sync_trans_date    timestamp        NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp        NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp        NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.contact.contact_id IS 'Contact Id is a unique Id of a contact from CIRR_CONTACTS.CON_ID'
;
COMMENT ON COLUMN cuws.contact.first_name IS 'First Name is the first name of the contact from CIRR_CONTACTS.FIRST_NAME'
;
COMMENT ON COLUMN cuws.contact.last_name IS 'Last Name is the last name of the contact from CIRR_CONTACTS.FIRST_LAST'
;
COMMENT ON COLUMN cuws.contact.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.contact.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.contact.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.contact.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.contact.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.contact IS 'The table contains all contacts from CIRR_CONTACTS'
;

ALTER TABLE cuws.contact ADD 
    CONSTRAINT pk_co PRIMARY KEY (contact_id) USING INDEX TABLESPACE pg_default
;
