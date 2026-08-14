CREATE OR REPLACE FUNCTION cuws.fn_inventory_field_audit()
RETURNS TRIGGER AS $$
BEGIN
	IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.inventory_field_audit (
            inventory_field_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_field_guid,
            insurance_plan_id,
            field_id,
            last_year_crop_commodity_id,
            last_year_crop_variety_id,
            underseeded_crop_variety_id,
            underseeded_inventory_seeded_forage_guid,
            crop_year,
            planting_number,
            is_hidden_on_printout_ind,
            underseeded_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.ifa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_field_guid,
            OLD.insurance_plan_id,
            OLD.field_id,
            OLD.last_year_crop_commodity_id,
            OLD.last_year_crop_variety_id,
            OLD.underseeded_crop_variety_id,
            OLD.underseeded_inventory_seeded_forage_guid,
            OLD.crop_year,
            OLD.planting_number,
            OLD.is_hidden_on_printout_ind,
            OLD.underseeded_acres,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_field_audit (
            inventory_field_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_field_guid,
            insurance_plan_id,
            field_id,
            last_year_crop_commodity_id,
            last_year_crop_variety_id,
            underseeded_crop_variety_id,
            underseeded_inventory_seeded_forage_guid,
            crop_year,
            planting_number,
            is_hidden_on_printout_ind,
            underseeded_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.ifa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_field_guid,
            NEW.insurance_plan_id,
            NEW.field_id,
            NEW.last_year_crop_commodity_id,
            NEW.last_year_crop_variety_id,
            NEW.underseeded_crop_variety_id,
            NEW.underseeded_inventory_seeded_forage_guid,
            NEW.crop_year,
            NEW.planting_number,
            NEW.is_hidden_on_printout_ind,
            NEW.underseeded_acres,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_field_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_field
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_field_audit();

