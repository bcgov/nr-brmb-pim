CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_field_rollup_forage_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_field_rollup_forage_audit (
            declared_yield_field_rollup_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_rollup_forage_guid,
            declared_yield_contract_guid,
            commodity_type_code,
            total_field_acres,
            total_bales_loads,
            harvested_acres,
            quantity_harvested_tons,
            yield_per_acre,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfrfa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_field_rollup_forage_guid,
            OLD.declared_yield_contract_guid,
            OLD.commodity_type_code,
            OLD.total_field_acres,
            OLD.total_bales_loads,
            OLD.harvested_acres,
            OLD.quantity_harvested_tons,
            OLD.yield_per_acre,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.declared_yield_field_rollup_forage_audit (
            declared_yield_field_rollup_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_rollup_forage_guid,
            declared_yield_contract_guid,
            commodity_type_code,
            total_field_acres,
            total_bales_loads,
            harvested_acres,
            quantity_harvested_tons,
            yield_per_acre,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfrfa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_field_rollup_forage_guid,
            NEW.declared_yield_contract_guid,
            NEW.commodity_type_code,
            NEW.total_field_acres,
            NEW.total_bales_loads,
            NEW.harvested_acres,
            NEW.quantity_harvested_tons,
            NEW.yield_per_acre,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_field_rollup_forage_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_field_rollup_forage
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_field_rollup_forage_audit();

