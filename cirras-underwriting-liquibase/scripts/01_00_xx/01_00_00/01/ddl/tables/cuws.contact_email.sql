-- 
-- TABLE: cuws.contact_email 
--

CREATE TABLE cuws.contact_email(
    contact_email_id        numeric(9, 0)    NOT NULL,
    contact_id              numeric(9, 0)    NOT NULL,
    email_address           varchar(100)     NOT NULL,
    is_primary_email_ind    varchar(1)       NOT NULL,
    effective_date          date             NOT NULL,
    expiry_date             date             NOT NULL,
    data_sync_trans_date    timestamp        NOT NULL,
    create_user             varchar(64)      NOT NULL,
    create_date             timestamp        NOT NULL,
    update_user             varchar(64)      NOT NULL,
    update_date             timestamp        NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.contact_email.contact_email_id IS 'Email Id is a unique Id of a contact from CIRR_EMAILS.EM_ID'
;
COMMENT ON COLUMN cuws.contact_email.contact_id IS 'Contact Id is a unique Id of a contact from CIRR_CONTACTS.CON_ID'
;
COMMENT ON COLUMN cuws.contact_email.email_address IS 'Email Address is the email address from CIRR_EMAILS.ADDRESS'
;
COMMENT ON COLUMN cuws.contact_email.is_primary_email_ind IS 'Is Primary Email Ind indicates if the email is the primary email address of a contact from CIRR_EMAILS.PRIMARY_EMAIL_FLAG'
;
COMMENT ON COLUMN cuws.contact_email.effective_date IS 'Effective Date is the date when the record was first active'
;
COMMENT ON COLUMN cuws.contact_email.expiry_date IS 'Expiry Date is the date when the record was last active'
;
COMMENT ON COLUMN cuws.contact_email.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.contact_email.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.contact_email.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.contact_email.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.contact_email.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.contact_email IS 'The table contains all e-mail addresses from CIRR_EMAILS'
;

ALTER TABLE cuws.contact_email ADD 
    CONSTRAINT pk_ce PRIMARY KEY (contact_email_id) USING INDEX TABLESPACE pg_default
;

CREATE INDEX ix_ce_co ON cuws.contact_email(contact_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.contact_email ADD CONSTRAINT fk_ce_co 
    FOREIGN KEY (contact_id)
    REFERENCES cuws.contact(contact_id)
;

