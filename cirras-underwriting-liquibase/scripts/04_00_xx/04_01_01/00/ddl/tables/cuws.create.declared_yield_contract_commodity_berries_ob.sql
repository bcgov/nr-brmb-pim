CREATE TABLE cuws.declared_yield_contract_commodity_berries_ob(
    dyccb_ob_id                                       numeric(10, 0)    NOT NULL,
    audit_transaction_type_code                       varchar(10)       NOT NULL,
    declared_yield_contract_commodity_berries_guid    varchar(32)       NOT NULL,
    create_user                                       varchar(32)       NOT NULL,
    create_date                                       timestamp(0)      NOT NULL,
    update_user                                       varchar(32)       NOT NULL,
    update_date                                       timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.dyccb_ob_id IS 'dyccb ob id is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.audit_transaction_type_code IS 'Audit Transaction Type Code is a unique type code for each audit transaction such as INSERT, UPDATE or DELETE'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.declared_yield_contract_commodity_berries_guid IS 'Declared Yield Field Commodity Berries Guid  is the primary key used to identify the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_contract_commodity_berries_ob.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_contract_commodity_berries_ob IS 'The table contains the primary key of inserted, updated and deleted records to be picked up by the messaging queue'
;

CREATE INDEX ix_dyccbo_attc ON cuws.declared_yield_contract_commodity_berries_ob(audit_transaction_type_code)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_contract_commodity_berries_ob ADD 
    CONSTRAINT pk_dyccbo PRIMARY KEY (dyccb_ob_id) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_contract_commodity_berries_ob ADD CONSTRAINT fk_dyccbo_attc 
    FOREIGN KEY (audit_transaction_type_code)
    REFERENCES cuws.audit_transaction_type_code(audit_transaction_type_code)
;
