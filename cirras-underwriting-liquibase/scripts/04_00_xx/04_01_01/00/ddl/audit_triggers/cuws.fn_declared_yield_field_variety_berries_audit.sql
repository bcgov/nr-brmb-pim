CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_field_variety_berries_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_field_variety_berries_audit (
            declared_yield_field_variety_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_variety_berries_guid,
            declared_yield_field_commodity_berries_guid,
            crop_variety_id,
            planted_acres,
            mature_equivalent_acres,
            sold_shipped_yield,
            sales_yield,
            abandonment_yield,
            total_production,
            total_production_override,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfvba_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_field_variety_berries_guid,
            OLD.declared_yield_field_commodity_berries_guid,
            OLD.crop_variety_id,
            OLD.planted_acres,
            OLD.mature_equivalent_acres,
            OLD.sold_shipped_yield,
            OLD.sales_yield,
            OLD.abandonment_yield,
            OLD.total_production,
            OLD.total_production_override,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.declared_yield_field_variety_berries_audit (
            declared_yield_field_variety_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_variety_berries_guid,
            declared_yield_field_commodity_berries_guid,
            crop_variety_id,
            planted_acres,
            mature_equivalent_acres,
            sold_shipped_yield,
            sales_yield,
            abandonment_yield,
            total_production,
            total_production_override,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfvba_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_field_variety_berries_guid,
            NEW.declared_yield_field_commodity_berries_guid,
            NEW.crop_variety_id,
            NEW.planted_acres,
            NEW.mature_equivalent_acres,
            NEW.sold_shipped_yield,
            NEW.sales_yield,
            NEW.abandonment_yield,
            NEW.total_production,
            NEW.total_production_override,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_field_variety_berries_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_field_variety_berries
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_field_variety_berries_audit();
