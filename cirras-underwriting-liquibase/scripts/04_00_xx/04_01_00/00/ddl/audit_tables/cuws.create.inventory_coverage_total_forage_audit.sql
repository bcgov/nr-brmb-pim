CREATE TABLE cuws.inventory_coverage_total_forage_audit(
    inventory_coverage_total_forage_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code                 varchar(10)       NOT NULL,
    audit_time_stamp                            timestamp(6)      NOT NULL,
    inventory_coverage_total_forage_guid        varchar(32)       NOT NULL,
    inventory_contract_guid                     varchar(32)       NOT NULL,
    crop_commodity_id                           numeric(9, 0),
    plant_insurability_type_code                varchar(10),
    is_unseeded_insurable_ind                   varchar(1),
    total_field_acres                           numeric(10, 4),
    create_user                                 varchar(64)       NOT NULL,
    create_date                                 timestamp(0)      NOT NULL,
    update_user                                 varchar(64)       NOT NULL,
    update_date                                 timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.inventory_coverage_total_forage_audit_id IS 'Inventory Coverage Total Forage Audit Id is the ID of the Inventory Coverage Total Forage Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.inventory_coverage_total_forage_guid IS 'Inventory Coverage Total Forage GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.inventory_contract_guid IS 'Inventory Contract GUID links to a record in INVENTORY_CONTRACT table'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.crop_commodity_id IS 'Crop Commodity Id is the unique identifier for the Crop Type from CROP_COMMODITY'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.plant_insurability_type_code IS 'Plant Insurability Type Code is a unique record identifier for plant insurability type records.'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.is_unseeded_insurable_ind IS 'Is Unseeded Insurable Ind determines if the specified record is the total insurable for Unseeded Coverage(Y) or not (N). '
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.total_field_acres IS 'Total Field Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_coverage_total_forage_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_coverage_total_forage_audit IS 'Inventory Coverage Total Forage Audit is the audit table for inventory_coverage_total_forage'
;

ALTER TABLE cuws.inventory_coverage_total_forage_audit ADD 
    CONSTRAINT pk_ictfa PRIMARY KEY (inventory_coverage_total_forage_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_coverage_total_forage_audit ADD CONSTRAINT fk_ictfa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


