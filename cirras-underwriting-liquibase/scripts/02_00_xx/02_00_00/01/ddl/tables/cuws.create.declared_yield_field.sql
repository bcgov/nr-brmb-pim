

CREATE TABLE cuws.declared_yield_field(
    declared_yield_field_guid                varchar(32)       NOT NULL,
    inventory_field_guid                     varchar(32)       NOT NULL,
    estimated_yield_per_acre                 numeric(14, 4),
    estimated_yield_per_acre_default_unit    numeric(14, 4),
    unharvested_acres_ind                    varchar(1)        NOT NULL,
    create_user                              varchar(64)       NOT NULL,
    create_date                              timestamp(0)      NOT NULL,
    update_user                              varchar(64)       NOT NULL,
    update_date                              timestamp(0)      NOT NULL
) TABLESPACE pg_default
;



COMMENT ON COLUMN cuws.declared_yield_field.declared_yield_field_guid IS 'Declared Yield Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.declared_yield_field.inventory_field_guid IS 'Inventory Field GUID links to a record in INVENTORY_FIELD table'
;
COMMENT ON COLUMN cuws.declared_yield_field.estimated_yield_per_acre IS 'Estimated Yield Per Acre is the declared estimated amount of yield produced for 1 acre, in DECLARED_YIELD_CONTRACT.ENTERED_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_field.estimated_yield_per_acre_default_unit IS 'Estimated Yield Per Acre Default Unit is the declared estimated amount of yield produced for 1 acre, in DECLARED_YIELD_CONTRACT.DEFAULT_YIELD_MEAS_UNIT_TYPE_CODE units.'
;
COMMENT ON COLUMN cuws.declared_yield_field.unharvested_acres_ind IS 'Unharvested Acres Ind is Y if there are acres of a planting that the grower declares that were not harvested.'
;
COMMENT ON COLUMN cuws.declared_yield_field.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field IS 'Declared Yield Field contains Declaration of Production at the Planting level.'
;

ALTER TABLE cuws.declared_yield_field ADD 
    CONSTRAINT pk_dyf PRIMARY KEY (declared_yield_field_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field ADD 
    CONSTRAINT uk_dyf UNIQUE (inventory_field_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field ADD CONSTRAINT fk_dyf_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;


