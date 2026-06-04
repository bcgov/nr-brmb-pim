CREATE OR REPLACE FUNCTION cuws.fn_verified_yield_amendment_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.verified_yield_amendment_audit (
            verified_yield_amendment_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_amendment_guid,
            verified_yield_amendment_code,
            verified_yield_contract_guid,
            crop_commodity_id,
            crop_variety_id,
            is_pedigree_ind,
            field_id,
            yield_per_acre,
            acres,
            rationale,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vyaa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.verified_yield_amendment_guid,
            OLD.verified_yield_amendment_code,
            OLD.verified_yield_contract_guid,
            OLD.crop_commodity_id,
            OLD.crop_variety_id,
            OLD.is_pedigree_ind,
            OLD.field_id,
            OLD.yield_per_acre,
            OLD.acres,
            OLD.rationale,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSE
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.verified_yield_amendment_audit (
            verified_yield_amendment_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_amendment_guid,
            verified_yield_amendment_code,
            verified_yield_contract_guid,
            crop_commodity_id,
            crop_variety_id,
            is_pedigree_ind,
            field_id,
            yield_per_acre,
            acres,
            rationale,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vyaa_seq'),
            TG_OP, -- This will be 'INSERT' or 'UPDATE'
            CURRENT_TIMESTAMP,
            NEW.verified_yield_amendment_guid,
            NEW.verified_yield_amendment_code,
            NEW.verified_yield_contract_guid,
            NEW.crop_commodity_id,
            NEW.crop_variety_id,
            NEW.is_pedigree_ind,
            NEW.field_id,
            NEW.yield_per_acre,
            NEW.acres,
            NEW.rationale,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_verified_yield_amendment_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.verified_yield_amendment
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_verified_yield_amendment_audit();
