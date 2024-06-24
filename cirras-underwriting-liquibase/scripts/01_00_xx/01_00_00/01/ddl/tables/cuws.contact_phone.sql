
-- 
-- TABLE: cuws.contact_phone 
--

CREATE TABLE cuws.contact_phone(
    contact_phone_id        numeric(9, 0)    NOT NULL,
    contact_id              numeric(9, 0)    NOT NULL,
    phone_number            varchar(10)      NOT NULL,
    extension               varchar(4),
    is_primary_phone_ind    varchar(1)       NOT NULL,
    effective_date          date             NOT NULL,
    expiry_date             date             NOT NULL,
    data_sync_trans_date    timestamp        NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp        NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp        NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.contact_phone.contact_phone_id IS 'Contact Phone Id is a unique Id of a phone number from CIRR_TELECOMMUNICATIONS.TCM_ID'
;
COMMENT ON COLUMN cuws.contact_phone.contact_id IS 'Contact Id is a unique Id of a contact from CIRR_CONTACTS.CON_ID'
;
COMMENT ON COLUMN cuws.contact_phone.phone_number IS 'Phone Number is the phone number concatenated from CIRR_TELECOMMUNICATIONS.AREA_CODE, PREFIX, SUFFIX'
;
COMMENT ON COLUMN cuws.contact_phone.extension IS 'Extension is the extension of a phone number from CIRR_TELECOMMUNICATIONS.EXTENSION'
;
COMMENT ON COLUMN cuws.contact_phone.is_primary_phone_ind IS 'Is Primary Phone Ind indicates if the number is the primary phone numer of a contact from CIRR_TELECOMMUNICATIONS.PRIMARY_TELECOMMUNICATION_FLAG'
;
COMMENT ON COLUMN cuws.contact_phone.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.contact_phone.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.contact_phone.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.contact_phone.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.contact_phone.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.contact_phone.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.contact_phone.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.contact_phone IS 'The table contains all phone numbers of contacts from CIRR_TELECOMMUNICATIONS'
;


ALTER TABLE cuws.contact_phone ADD 
    CONSTRAINT pk_cp PRIMARY KEY (contact_phone_id) USING INDEX TABLESPACE pg_default
;

CREATE INDEX ix_cp_co ON cuws.contact_phone(contact_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.contact_phone ADD CONSTRAINT fk_cp_co 
    FOREIGN KEY (contact_id)
    REFERENCES cuws.contact(contact_id)
;
