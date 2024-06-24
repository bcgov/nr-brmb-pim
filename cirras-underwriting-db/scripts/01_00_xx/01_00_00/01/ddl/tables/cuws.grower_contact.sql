
-- 
-- TABLE: cuws.grower_contact 
--

CREATE TABLE cuws.grower_contact(
    grower_contact_id         numeric(9, 0)    NOT NULL,
    contact_id                numeric(9, 0)    NOT NULL,
    grower_id                 numeric(9, 0)    NOT NULL,
    is_primary_contact_ind    varchar(1)       NOT NULL,
    is_actively_involved_ind  varchar(1)       NOT NULL,
    data_sync_trans_date      timestamp        NOT NULL,
    create_user               varchar(64)      NOT NULL,
    create_date               timestamp        NOT NULL,
    update_user               varchar(64)      NOT NULL,
    update_date               timestamp        NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.grower_contact.grower_contact_id IS 'Grower Contact Id is a unique Id of a grower contact record from CIRR_INSURED_GROWER_CONTACTS.IGC_ID'
;
COMMENT ON COLUMN cuws.grower_contact.contact_id IS 'Contact Id is a unique Id of a contact from CIRR_CONTACTS.CON_ID'
;
COMMENT ON COLUMN cuws.grower_contact.grower_id IS 'Grower Id is a unique key of a grower from CIRR_INSURED_GROWERS.IG_ID'
;
COMMENT ON COLUMN cuws.grower_contact.is_primary_contact_ind IS 'Is Primary Contact Ind indicates if the contact is the primary contact of a grower from CIRR_INSURED_GROWER_CONTACTS.PRIMARY_CONTACT_FLAG'
;
COMMENT ON COLUMN cuws.grower_contact.is_actively_involved_ind IS 'Is Actively Involved Ind indicates if the contact is actively involved for the grower from CIRR_INSURED_GROWER_CONTACTS.ACTIVELY_INVOLVED_FLAG'
;
COMMENT ON COLUMN cuws.grower_contact.data_sync_trans_date IS 'Data Sync Trans Date is the date and time when the data has been updated in the source system. This prevents out of date updates'
;
COMMENT ON COLUMN cuws.grower_contact.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.grower_contact.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.grower_contact.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.grower_contact.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.grower_contact IS 'The table contains all records from CIRR_INSURED_GROWER_CONTACTS and maps contacts (CONTACT) to growers (GROWER)'
;

ALTER TABLE cuws.grower_contact ADD 
    CONSTRAINT pk_gc PRIMARY KEY (grower_contact_id) USING INDEX TABLESPACE pg_default
;

CREATE INDEX ix_gc_co ON cuws.grower_contact(contact_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_gc_gwr ON cuws.grower_contact(grower_id)
 TABLESPACE pg_default
;

ALTER TABLE cuws.grower_contact ADD 
    CONSTRAINT uk_gc UNIQUE (contact_id, grower_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.grower_contact ADD CONSTRAINT fk_gc_co 
    FOREIGN KEY (contact_id)
    REFERENCES cuws.contact(contact_id)
;

ALTER TABLE cuws.grower_contact ADD CONSTRAINT fk_gc_gwr 
    FOREIGN KEY (grower_id)
    REFERENCES cuws.grower(grower_id)
;


