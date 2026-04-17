CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_contract_commodity_berries_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_contract_commodity_berries_audit (
            declared_yield_contract_commodity_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_contract_commodity_berries_guid,
            declared_yield_contract_guid,
            crop_commodity_id,
            total_production,
            total_production_override,
            total_planted_acres,
            total_mature_equivalent_acres,
            total_sold_shipped_yield,
            total_sales_yield,
            total_abandonment_yield,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyccba_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_contract_commodity_berries_guid,
            OLD.declared_yield_contract_guid,
            OLD.crop_commodity_id,
            OLD.total_production,
            OLD.total_production_override,
            OLD.total_planted_acres,
            OLD.total_mature_equivalent_acres,
            OLD.total_sold_shipped_yield,
            OLD.total_sales_yield,
            OLD.total_abandonment_yield,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
	ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.declared_yield_contract_commodity_berries_audit (
            declared_yield_contract_commodity_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_contract_commodity_berries_guid,
            declared_yield_contract_guid,
            crop_commodity_id,
            total_production,
            total_production_override,
            total_planted_acres,
            total_mature_equivalent_acres,
            total_sold_shipped_yield,
            total_sales_yield,
            total_abandonment_yield,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyccba_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_contract_commodity_berries_guid,
            NEW.declared_yield_contract_guid,
            NEW.crop_commodity_id,
            NEW.total_production,
            NEW.total_production_override,
            NEW.total_planted_acres,
            NEW.total_mature_equivalent_acres,
            NEW.total_sold_shipped_yield,
            NEW.total_sales_yield,
            NEW.total_abandonment_yield,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_declared_yield_contract_commodity_berries_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_contract_commodity_berries
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_contract_commodity_berries_audit();

