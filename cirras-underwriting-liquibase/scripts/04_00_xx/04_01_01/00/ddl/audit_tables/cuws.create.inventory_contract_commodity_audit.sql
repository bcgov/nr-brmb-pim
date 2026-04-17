CREATE TABLE cuws.inventory_contract_commodity_audit(
    inventory_contract_commodity_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code              varchar(10)       NOT NULL,
    audit_time_stamp                         timestamp(6)      NOT NULL,
    inventory_contract_commodity_guid        varchar(32)       NOT NULL,
    inventory_contract_guid                  varchar(32),
    crop_commodity_id                        numeric(9, 0),
    is_pedigree_ind                          varchar(1),
    total_unseeded_acres                     numeric(10, 4),
    total_unseeded_acres_override            numeric(10, 4),
    total_seeded_acres                       numeric(10, 4),
    total_spot_loss_acres                    numeric(10, 4),
    create_user                              varchar(64)       NOT NULL,
    create_date                              timestamp(6)      NOT NULL,
    update_user                              varchar(64)       NOT NULL,
    update_date                              timestamp(6)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.inventory_contract_commodity_audit_id IS 'Inventory Contract Commodity Audit Id is the ID of the Inventory Contract Commodity Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.inventory_contract_commodity_guid IS 'Inventory Contract Commodity GUID  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.crop_commodity_id IS 'Crop Commodity Id is a unique Id of a commodity from cirr_crop_types.crpt_id'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.is_pedigree_ind IS 'Is Pedigree Ind determines if the acres is for a pedigree crop (Y) or (N).'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.total_unseeded_acres IS 'Total Unseeded Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.total_unseeded_acres_override IS 'Total Unseeded Acres Override are the overridden total unseeded acres'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.total_seeded_acres IS 'Total Seeded Acres is the number of acres utilized by the planted crop'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.total_spot_loss_acres IS 'Total Spot Loss Acres is the number of seeded acres that are insurable for Grain Spot Loss'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.inventory_contract_commodity_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.inventory_contract_commodity_audit IS 'Inventory Contract Commodity Audit is the audit table for inventory_contract_commodity'
;

ALTER TABLE cuws.inventory_contract_commodity_audit ADD 
    CONSTRAINT pk_icca PRIMARY KEY (inventory_contract_commodity_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.inventory_contract_commodity_audit ADD CONSTRAINT fk_icca_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


