CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_field_commodity_berries_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_field_commodity_berries_audit (
            declared_yield_field_commodity_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_commodity_berries_guid,
            field_id,
            crop_commodity_id,
            crop_year,
            total_production,
            total_production_override,
            total_planted_acres,
            total_mature_equivalent_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfcba_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_field_commodity_berries_guid,
            OLD.field_id,
            OLD.crop_commodity_id,
            OLD.crop_year,
            OLD.total_production,
            OLD.total_production_override,
            OLD.total_planted_acres,
            OLD.total_mature_equivalent_acres,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.declared_yield_field_commodity_berries_audit (
            declared_yield_field_commodity_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_commodity_berries_guid,
            field_id,
            crop_commodity_id,
            crop_year,
            total_production,
            total_production_override,
            total_planted_acres,
            total_mature_equivalent_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfcba_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_field_commodity_berries_guid,
            NEW.field_id,
            NEW.crop_commodity_id,
            NEW.crop_year,
            NEW.total_production,
            NEW.total_production_override,
            NEW.total_planted_acres,
            NEW.total_mature_equivalent_acres,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_field_commodity_berries_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_field_commodity_berries
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_field_commodity_berries_audit();

