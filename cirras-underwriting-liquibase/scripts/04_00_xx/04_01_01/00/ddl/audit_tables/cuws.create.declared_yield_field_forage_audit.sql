CREATE TABLE cuws.declared_yield_field_forage_audit(
    declared_yield_field_forage_audit_id    numeric(9, 0)     NOT NULL,
    audit_transaction_type_code             varchar(10)       NOT NULL,
    audit_time_stamp                        timestamp(6)      NOT NULL,
    declared_yield_field_forage_guid        varchar(32)       NOT NULL,
    inventory_field_guid                    varchar(32),
    cut_number                              numeric(2, 0),
    total_bales_loads                       numeric(4, 0),
    weight                                  numeric(14, 4),
    weight_default_unit                     numeric(14, 4),
    moisture_percent                        numeric(14, 4),
    create_user                             varchar(64)       NOT NULL,
    create_date                             timestamp(0)      NOT NULL,
    update_user                             varchar(64)       NOT NULL,
    update_date                             timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.declared_yield_field_forage_audit_id IS 'Declared Yield Field Forage Audit Id is the ID of the Declared Yield Field Forage Audit table, comes from a sequence.'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.audit_time_stamp IS 'Audit Time Stamp denotes when the record was inserted in the audit table.'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.declared_yield_field_forage_guid IS 'Declared Yield Field Forage GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.cut_number IS 'Cut Number is the number of the cut in a crop year'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.total_bales_loads IS 'Total Bales Loads is the number of bales or loads harvested on the field'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.weight IS 'Weight is the total weight in the selected units'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.weight_default_unit IS 'Weight is the total weight in the default units of the insurance plan'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.moisture_percent IS 'Moisture Percent is the percentage of water in the crops'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage_audit.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_forage_audit IS 'Declared Yield Field Forage Audit is the audit table for declared_yield_field_forage'
;

ALTER TABLE cuws.declared_yield_field_forage_audit ADD 
    CONSTRAINT pk_dyffa PRIMARY KEY (declared_yield_field_forage_audit_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_forage_audit ADD CONSTRAINT pk_dyffa_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;


