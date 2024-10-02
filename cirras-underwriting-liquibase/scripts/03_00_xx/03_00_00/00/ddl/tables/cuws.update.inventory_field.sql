

--Add new COLUMNS
ALTER TABLE cuws.inventory_field ADD COLUMN underseeded_inventory_seeded_forage_guid varchar(32);

COMMENT ON COLUMN cuws.inventory_field.underseeded_inventory_seeded_forage_guid IS 'Inventory Seeded Forage GUID is the primary key used to identify the record'
;

ALTER TABLE cuws.inventory_field ADD CONSTRAINT fk_if_isf 
    FOREIGN KEY (underseeded_inventory_seeded_forage_guid)
    REFERENCES cuws.inventory_seeded_forage(inventory_seeded_forage_guid)
;

ALTER TABLE cuws.inventory_field ADD 
    CONSTRAINT uk_if_isf UNIQUE (underseeded_inventory_seeded_forage_guid) USING INDEX TABLESPACE pg_default 
;
