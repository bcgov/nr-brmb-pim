CREATE OR REPLACE FUNCTION cuws.fn_inventory_contract_commodity_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.inventory_contract_commodity_audit (
            inventory_contract_commodity_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_contract_commodity_guid,
            inventory_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            total_unseeded_acres,
            total_unseeded_acres_override,
            total_seeded_acres,
            total_spot_loss_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.icca_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_contract_commodity_guid,
            OLD.inventory_contract_guid,
            OLD.crop_commodity_id,
            OLD.is_pedigree_ind,
            OLD.total_unseeded_acres,
            OLD.total_unseeded_acres_override,
            OLD.total_seeded_acres,
            OLD.total_spot_loss_acres,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.inventory_contract_commodity_audit (
            inventory_contract_commodity_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_contract_commodity_guid,
            inventory_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            total_unseeded_acres,
            total_unseeded_acres_override,
            total_seeded_acres,
            total_spot_loss_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.icca_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_contract_commodity_guid,
            NEW.inventory_contract_guid,
            NEW.crop_commodity_id,
            NEW.is_pedigree_ind,
            NEW.total_unseeded_acres,
            NEW.total_unseeded_acres_override,
            NEW.total_seeded_acres,
            NEW.total_spot_loss_acres,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_contract_commodity_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_contract_commodity
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_contract_commodity_audit();
