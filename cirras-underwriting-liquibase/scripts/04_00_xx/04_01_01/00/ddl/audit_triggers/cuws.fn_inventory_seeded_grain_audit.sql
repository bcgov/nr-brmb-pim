CREATE OR REPLACE FUNCTION cuws.fn_inventory_seeded_grain_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.inventory_seeded_grain_audit (
            inventory_seeded_grain_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_seeded_grain_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            commodity_type_code,
            is_quantity_insurable_ind,
            is_spot_loss_insurable_ind,
            is_replaced_ind,
            is_pedigree_ind,
            seeding_date,
            seeded_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.isga_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_seeded_grain_guid,
            OLD.inventory_field_guid,
            OLD.crop_commodity_id,
            OLD.crop_variety_id,
            OLD.commodity_type_code,
            OLD.is_quantity_insurable_ind,
            OLD.is_spot_loss_insurable_ind,
            OLD.is_replaced_ind,
            OLD.is_pedigree_ind,
            OLD.seeding_date,
            OLD.seeded_acres,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
	ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_seeded_grain_audit (
            inventory_seeded_grain_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_seeded_grain_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            commodity_type_code,
            is_quantity_insurable_ind,
            is_spot_loss_insurable_ind,
            is_replaced_ind,
            is_pedigree_ind,
            seeding_date,
            seeded_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.isga_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_seeded_grain_guid,
            NEW.inventory_field_guid,
            NEW.crop_commodity_id,
            NEW.crop_variety_id,
            NEW.commodity_type_code,
            NEW.is_quantity_insurable_ind,
            NEW.is_spot_loss_insurable_ind,
            NEW.is_replaced_ind,
            NEW.is_pedigree_ind,
            NEW.seeding_date,
            NEW.seeded_acres,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_inventory_seeded_grain_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_seeded_grain
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_seeded_grain_audit();

