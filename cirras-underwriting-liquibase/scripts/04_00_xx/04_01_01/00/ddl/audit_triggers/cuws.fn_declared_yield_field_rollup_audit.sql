CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_field_rollup_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_field_rollup_audit (
            declared_yield_field_rollup_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_rollup_guid,
            declared_yield_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            estimated_yield_per_acre_tonnes,
            estimated_yield_per_acre_bushels,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfra_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_field_rollup_guid,
            OLD.declared_yield_contract_guid,
            OLD.crop_commodity_id,
            OLD.is_pedigree_ind,
            OLD.estimated_yield_per_acre_tonnes,
            OLD.estimated_yield_per_acre_bushels,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.declared_yield_field_rollup_audit (
            declared_yield_field_rollup_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_field_rollup_guid,
            declared_yield_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            estimated_yield_per_acre_tonnes,
            estimated_yield_per_acre_bushels,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyfra_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_field_rollup_guid,
            NEW.declared_yield_contract_guid,
            NEW.crop_commodity_id,
            NEW.is_pedigree_ind,
            NEW.estimated_yield_per_acre_tonnes,
            NEW.estimated_yield_per_acre_bushels,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_declared_yield_field_rollup_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_field_rollup
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_field_rollup_audit();

