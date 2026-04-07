CREATE TABLE cuws.legal_land_audit(
    legal_land_audit_id                  numeric(9, 0)     NOT NULL,
    audit_transaction_type_code          varchar(10)       NOT NULL,
    audit_time_stamp                     timestamp(6)      NOT NULL,
    legal_land_id                        numeric(10, 0)    NOT NULL,
    primary_property_identifier          varchar(50),
    primary_land_identifier_type_code    varchar(10),
    primary_reference_type_code          varchar(10),
    legal_description                    varchar(2000),
    legal_short_description              varchar(500),
    other_description                    varchar(128),
    total_acres                          numeric(10, 4),
    active_from_crop_year                numeric(4, 0),
    active_to_crop_year                  numeric(4, 0),
    create_user                          varchar(64)       NOT NULL,
    create_date                          timestamp(6)      NOT NULL,
    update_user                          varchar(64)       NOT NULL,
    update_date                          timestamp(6)      NOT NULL
)
;



COMMENT ON COLUMN cuws.legal_land_audit.legal_land_audit_id IS 'Legal Land Audit Id is the ID of the Legal Land Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.legal_land_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.legal_land_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.legal_land_audit.legal_land_id IS 'Legal Land Id is a unique key of a legal land from cirr_legal_land.legal_land_id'
;
COMMENT ON COLUMN cuws.legal_land_audit.primary_property_identifier IS 'PRIMARY PROPERTY IDENTIFIER is a unique identifier assigned to a property that makes up part or all of a lot. The PROPERTY IDENTIFIER is a unique alpha-numeric assigned to a property outside of the CIRRAS system.'
;
COMMENT ON COLUMN cuws.legal_land_audit.primary_land_identifier_type_code IS 'Primary Land Identifier Type Code is the type of Primary Property Identifier: PID, PIN, IR, LINC or OTHER.'
;
COMMENT ON COLUMN cuws.legal_land_audit.primary_reference_type_code IS 'Primary Reference Type Code is a code value that uniquely identifies the record from cirr_legal_land.primary_reference_type_code'
;
COMMENT ON COLUMN cuws.legal_land_audit.legal_description IS 'Legal Description is the description used by the BC Land Survey System for a lot of land. Populated from cirr_legal_land.legal_decription.'
;
COMMENT ON COLUMN cuws.legal_land_audit.legal_short_description IS 'Legal Short Description is a short form of the Legal Description from cirr_legal_land.short_legal_description. '
;
COMMENT ON COLUMN cuws.legal_land_audit.other_description IS 'Other Description is a text description used to describe legal land. Populated from cirr_legal_land.other_description'
;
COMMENT ON COLUMN cuws.legal_land_audit.total_acres IS 'Total Acres is the size of the legal land in acres.'
;
COMMENT ON COLUMN cuws.legal_land_audit.active_from_crop_year IS 'Active From Crop Year is the first year the legal land is active. Populated from cirr_legal_land.active_from_crop_year'
;
COMMENT ON COLUMN cuws.legal_land_audit.active_to_crop_year IS 'Active To Crop Year is the last year the legal land was active. Populated from cirr_legal_land.active_to_crop_year'
;
COMMENT ON COLUMN cuws.legal_land_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.legal_land_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.legal_land_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.legal_land_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.legal_land_audit IS 'The table contains all legal lands from CIRR_LEGAL_LAND'
;

ALTER TABLE cuws.legal_land_audit ADD 
    CONSTRAINT pk_lla PRIMARY KEY (legal_land_audit_id)
;

ALTER TABLE cuws.legal_land_audit ADD CONSTRAINT fk_lla_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


