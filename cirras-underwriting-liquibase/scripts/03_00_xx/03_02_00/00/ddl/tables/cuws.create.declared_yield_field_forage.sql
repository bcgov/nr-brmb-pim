

CREATE TABLE cuws.declared_yield_field_forage(
    declared_yield_field_forage_guid    varchar(32)       NOT NULL,
    inventory_field_guid                varchar(32)       NOT NULL,
    cut_number                          numeric(2, 0)     NOT NULL,
    total_bales_loads                   numeric(4, 0),
    weight                              numeric(14, 4),
    weight_default_unit                 numeric(14, 4),
    moisture_percent                    numeric(14, 4),
    create_user                         varchar(64)       NOT NULL,
    create_date                         timestamp(0)      NOT NULL,
    update_user                         varchar(64)       NOT NULL,
    update_date                         timestamp(0)      NOT NULL
) TABLESPACE pg_default
;

COMMENT ON COLUMN cuws.declared_yield_field_forage.declared_yield_field_forage_guid IS 'Declared Yield Field Forage GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.inventory_field_guid IS 'Inventory Field GUID is the primary key used to identify a table record. '
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.cut_number IS 'Cut Number is the number of the cut in a crop year'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.total_bales_loads IS 'Total Bales Loads is the number of bales or loads harvested on the field'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.weight IS 'Weight is the total weight in the selected units'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.weight_default_unit IS 'Weight is the total weight in the default units of the insurance plan'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.moisture_percent IS 'Moisture Percent is the percentage of water in the crops'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.create_user IS 'Create User is the user id of the user that created the record'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.create_date IS 'Create Date is the date when the record was created.'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.update_user IS 'Update User is the user id of the user that updated the record last'
;
COMMENT ON COLUMN cuws.declared_yield_field_forage.update_date IS 'Update Date is the date when the record was updated last.'
;
COMMENT ON TABLE cuws.declared_yield_field_forage IS 'The table contains declared yield at the Planting level for forage'
;

CREATE UNIQUE INDEX uk_dyff ON cuws.declared_yield_field_forage(inventory_field_guid, cut_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_dyff_if ON cuws.declared_yield_field_forage(inventory_field_guid)
 TABLESPACE pg_default
;
ALTER TABLE cuws.declared_yield_field_forage ADD 
    CONSTRAINT pk_dyff PRIMARY KEY (declared_yield_field_forage_guid) USING INDEX TABLESPACE pg_default 
;

ALTER TABLE cuws.declared_yield_field_forage ADD CONSTRAINT fk_dyff_if 
    FOREIGN KEY (inventory_field_guid)
    REFERENCES cuws.inventory_field(inventory_field_guid)
;