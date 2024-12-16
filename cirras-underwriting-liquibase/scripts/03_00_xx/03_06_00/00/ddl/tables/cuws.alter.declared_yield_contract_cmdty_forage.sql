--add two more digits to the total bales columns

ALTER TABLE cuws.declared_yield_contract_cmdty_forage ALTER COLUMN total_bales_loads TYPE numeric(6,0);
