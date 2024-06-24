

--Add new COLUMNS
ALTER TABLE cuws.inventory_contract ADD COLUMN inv_update_timestamp timestamp(0);
ALTER TABLE cuws.inventory_contract ADD COLUMN inv_update_user varchar(64);

COMMENT ON COLUMN cuws.inventory_contract.inv_update_timestamp IS 'Inv Update Timestamp is the last time any inventory data was changed by the user.'
;
COMMENT ON COLUMN cuws.inventory_contract.inv_update_user IS 'Inv Update User is the last user that changed any inventory data.'
;

--Set default values
UPDATE inventory_contract set 
inv_update_timestamp = update_date,
inv_update_user = update_user;

ALTER TABLE inventory_contract ALTER COLUMN inv_update_timestamp SET NOT NULL;
ALTER TABLE inventory_contract ALTER COLUMN inv_update_user SET NOT NULL;
