CREATE OR REPLACE FUNCTION cuws.fn_inventory_seeded_forage_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.inventory_seeded_forage_audit (
            inventory_seeded_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_seeded_forage_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            commodity_type_code,
            field_acres,
            seeding_year,
            seeding_date,
            is_irrigated_ind,
            is_quantity_insurable_ind,
            plant_insurability_type_code,
            is_awp_eligible_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.isfa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_seeded_forage_guid,
            OLD.inventory_field_guid,
            OLD.crop_commodity_id,
            OLD.crop_variety_id,
            OLD.commodity_type_code,
            OLD.field_acres,
            OLD.seeding_year,
            OLD.seeding_date,
            OLD.is_irrigated_ind,
            OLD.is_quantity_insurable_ind,
            OLD.plant_insurability_type_code,
            OLD.is_awp_eligible_ind,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
	ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_seeded_forage_audit (
            inventory_seeded_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_seeded_forage_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            commodity_type_code,
            field_acres,
            seeding_year,
            seeding_date,
            is_irrigated_ind,
            is_quantity_insurable_ind,
            plant_insurability_type_code,
            is_awp_eligible_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.isfa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_seeded_forage_guid,
            NEW.inventory_field_guid,
            NEW.crop_commodity_id,
            NEW.crop_variety_id,
            NEW.commodity_type_code,
            NEW.field_acres,
            NEW.seeding_year,
            NEW.seeding_date,
            NEW.is_irrigated_ind,
            NEW.is_quantity_insurable_ind,
            NEW.plant_insurability_type_code,
            NEW.is_awp_eligible_ind,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_seeded_forage_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_seeded_forage
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_seeded_forage_audit();


