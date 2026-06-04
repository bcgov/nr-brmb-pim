CREATE OR REPLACE FUNCTION cuws.fn_verified_yield_contract_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.verified_yield_contract_audit (
            verified_yield_contract_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_contract_guid,
            contract_id,
            crop_year,
            declared_yield_contract_guid,
            default_yield_meas_unit_type_code,
            verified_yield_update_timestamp,
            verified_yield_update_user,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vyca_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.verified_yield_contract_guid,
            OLD.contract_id,
            OLD.crop_year,
            OLD.declared_yield_contract_guid,
            OLD.default_yield_meas_unit_type_code,
            OLD.verified_yield_update_timestamp,
            OLD.verified_yield_update_user,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSE
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.verified_yield_contract_audit (
            verified_yield_contract_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_contract_guid,
            contract_id,
            crop_year,
            declared_yield_contract_guid,
            default_yield_meas_unit_type_code,
            verified_yield_update_timestamp,
            verified_yield_update_user,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vyca_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.verified_yield_contract_guid,
            NEW.contract_id,
            NEW.crop_year,
            NEW.declared_yield_contract_guid,
            NEW.default_yield_meas_unit_type_code,
            NEW.verified_yield_update_timestamp,
            NEW.verified_yield_update_user,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_verified_yield_contract_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.verified_yield_contract
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_verified_yield_contract_audit();

