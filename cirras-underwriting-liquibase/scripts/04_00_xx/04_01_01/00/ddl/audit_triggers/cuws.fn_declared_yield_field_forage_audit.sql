CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_field_forage_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_field_forage_audit (
            declared_yield_field_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_forage_guid,
            inventory_field_guid,
            cut_number,
            total_bales_loads,
            weight,
            weight_default_unit,
            moisture_percent,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyffa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_field_forage_guid,
            OLD.inventory_field_guid,
            OLD.cut_number,
            OLD.total_bales_loads,
            OLD.weight,
            OLD.weight_default_unit,
            OLD.moisture_percent,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.declared_yield_field_forage_audit (
            declared_yield_field_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_forage_guid,
            inventory_field_guid,
            cut_number,
            total_bales_loads,
            weight,
            weight_default_unit,
            moisture_percent,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyffa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_field_forage_guid,
            NEW.inventory_field_guid,
            NEW.cut_number,
            NEW.total_bales_loads,
            NEW.weight,
            NEW.weight_default_unit,
            NEW.moisture_percent,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_field_forage_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_field_forage
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_field_forage_audit();

