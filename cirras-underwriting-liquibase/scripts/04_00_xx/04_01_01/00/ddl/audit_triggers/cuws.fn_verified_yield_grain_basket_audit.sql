CREATE OR REPLACE FUNCTION cuws.fn_verified_yield_grain_basket_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.verified_yield_grain_basket_audit (
            verified_yield_grain_basket_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_grain_basket_guid,
            verified_yield_contract_guid,
            basket_value,
            total_quantity_coverage_value,
            total_coverage_value,
            harvested_value,
            comment,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vygba_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.verified_yield_grain_basket_guid,
            OLD.verified_yield_contract_guid,
            OLD.basket_value,
            OLD.total_quantity_coverage_value,
            OLD.total_coverage_value,
            OLD.harvested_value,
            OLD.comment,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSE
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.verified_yield_grain_basket_audit (
            verified_yield_grain_basket_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_grain_basket_guid,
            verified_yield_contract_guid,
            basket_value,
            total_quantity_coverage_value,
            total_coverage_value,
            harvested_value,
            comment,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vygba_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.verified_yield_grain_basket_guid,
            NEW.verified_yield_contract_guid,
            NEW.basket_value,
            NEW.total_quantity_coverage_value,
            NEW.total_coverage_value,
            NEW.harvested_value,
            NEW.comment,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_verified_yield_grain_basket_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.verified_yield_grain_basket
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_verified_yield_grain_basket_audit();

