


ALTER TABLE cuws.declared_yield_contract ADD COLUMN baler_wagon_info varchar(128);
ALTER TABLE cuws.declared_yield_contract ADD COLUMN total_livestock  numeric(5, 0);

COMMENT ON COLUMN cuws.declared_yield_contract.baler_wagon_info IS 'Baler Wagon Info is the make and model of a baler or wagon'
;
COMMENT ON COLUMN cuws.declared_yield_contract.total_livestock IS 'Total Livestock is the number of livestock'
;
