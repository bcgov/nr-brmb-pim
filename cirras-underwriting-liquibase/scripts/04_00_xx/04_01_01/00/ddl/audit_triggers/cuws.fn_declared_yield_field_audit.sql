CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_field_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_field_audit (
            declared_yield_field_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_guid,
            inventory_field_guid,
            estimated_yield_per_acre,
            estimated_yield_per_acre_default_unit,
            unharvested_acres_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_field_guid,
            OLD.inventory_field_guid,
            OLD.estimated_yield_per_acre,
            OLD.estimated_yield_per_acre_default_unit,
            OLD.unharvested_acres_ind,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.declared_yield_field_audit (
            declared_yield_field_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_guid,
            inventory_field_guid,
            estimated_yield_per_acre,
            estimated_yield_per_acre_default_unit,
            unharvested_acres_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfa_seq'),
            TG_OP, -- This will be 'INSERT' or 'UPDATE'
            CURRENT_TIMESTAMP,
            NEW.declared_yield_field_guid,
            NEW.inventory_field_guid,
            NEW.estimated_yield_per_acre,
            NEW.estimated_yield_per_acre_default_unit,
            NEW.unharvested_acres_ind,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_field_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_field
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_field_audit();

