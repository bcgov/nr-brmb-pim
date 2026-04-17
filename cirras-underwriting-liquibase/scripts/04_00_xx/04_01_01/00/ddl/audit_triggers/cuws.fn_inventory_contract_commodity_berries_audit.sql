CREATE OR REPLACE FUNCTION cuws.fn_inventory_contract_commodity_berries_audit()
RETURNS TRIGGER AS $$
BEGIN
	IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.inventory_contract_commodity_berries_audit (
            inventory_contract_commodity_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_contract_commodity_berries_guid,
            inventory_contract_guid,
            crop_commodity_id,
            total_insured_plants,
            total_uninsured_plants,
            total_quantity_insured_acres,
            total_quantity_uninsured_acres,
            total_plant_insured_acres,
            total_plant_uninsured_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.iccba_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_contract_commodity_berries_guid,
            OLD.inventory_contract_guid,
            OLD.crop_commodity_id,
            OLD.total_insured_plants,
            OLD.total_uninsured_plants,
            OLD.total_quantity_insured_acres,
            OLD.total_quantity_uninsured_acres,
            OLD.total_plant_insured_acres,
            OLD.total_plant_uninsured_acres,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;	
    ELSEIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_contract_commodity_berries_audit (
            inventory_contract_commodity_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_contract_commodity_berries_guid,
            inventory_contract_guid,
            crop_commodity_id,
            total_insured_plants,
            total_uninsured_plants,
            total_quantity_insured_acres,
            total_quantity_uninsured_acres,
            total_plant_insured_acres,
            total_plant_uninsured_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.iccba_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_contract_commodity_berries_guid,
            NEW.inventory_contract_guid,
            NEW.crop_commodity_id,
            NEW.total_insured_plants,
            NEW.total_uninsured_plants,
            NEW.total_quantity_insured_acres,
            NEW.total_quantity_uninsured_acres,
            NEW.total_plant_insured_acres,
            NEW.total_plant_uninsured_acres,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;  
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_contract_commodity_berries_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_contract_commodity_berries
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_contract_commodity_berries_audit();

