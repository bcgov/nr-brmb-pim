CREATE OR REPLACE FUNCTION cuws.fn_inventory_unseeded_audit()
RETURNS TRIGGER AS $$
BEGIN
	IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.inventory_unseeded_audit (
            inventory_unseeded_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_unseeded_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            is_unseeded_insurable_ind,
            acres_to_be_seeded,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.iua_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_unseeded_guid,
            OLD.inventory_field_guid,
            OLD.crop_commodity_id,
            OLD.crop_variety_id,
            OLD.is_unseeded_insurable_ind,
            OLD.acres_to_be_seeded,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_unseeded_audit (
            inventory_unseeded_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_unseeded_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            is_unseeded_insurable_ind,
            acres_to_be_seeded,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.iua_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_unseeded_guid,
            NEW.inventory_field_guid,
            NEW.crop_commodity_id,
            NEW.crop_variety_id,
            NEW.is_unseeded_insurable_ind,
            NEW.acres_to_be_seeded,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_unseeded_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_unseeded
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_unseeded_audit();
