--Alter COLUMNS
ALTER TABLE cuws.inventory_berries ADD COLUMN mature_equivalent_acres numeric(10, 4);

COMMENT ON COLUMN cuws.inventory_berries.mature_equivalent_acres IS 'Mature Equivalent Acres is the number of ME acres according to the planted year and scale'
;