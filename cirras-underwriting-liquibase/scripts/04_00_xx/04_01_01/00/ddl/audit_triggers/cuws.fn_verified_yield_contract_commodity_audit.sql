CREATE OR REPLACE FUNCTION cuws.fn_verified_yield_contract_commodity_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.verified_yield_contract_commodity_audit (
            verified_yield_contract_commodity_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_contract_commodity_guid,
            verified_yield_contract_guid,
            crop_commodity_id,
            commodity_type_code,
            is_pedigree_ind,
            harvested_acres,
            harvested_acres_override,
            stored_yield_default_unit,
            sold_yield_default_unit,
            production_guarantee,
            harvested_yield,
            harvested_yield_override,
            yield_per_acre,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vycca_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.verified_yield_contract_commodity_guid,
            OLD.verified_yield_contract_guid,
            OLD.crop_commodity_id,
            OLD.commodity_type_code,
            OLD.is_pedigree_ind,
            OLD.harvested_acres,
            OLD.harvested_acres_override,
            OLD.stored_yield_default_unit,
            OLD.sold_yield_default_unit,
            OLD.production_guarantee,
            OLD.harvested_yield,
            OLD.harvested_yield_override,
            OLD.yield_per_acre,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSE
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.verified_yield_contract_commodity_audit (
            verified_yield_contract_commodity_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_contract_commodity_guid,
            verified_yield_contract_guid,
            crop_commodity_id,
            commodity_type_code,
            is_pedigree_ind,
            harvested_acres,
            harvested_acres_override,
            stored_yield_default_unit,
            sold_yield_default_unit,
            production_guarantee,
            harvested_yield,
            harvested_yield_override,
            yield_per_acre,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vycca_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.verified_yield_contract_commodity_guid,
            NEW.verified_yield_contract_guid,
            NEW.crop_commodity_id,
            NEW.commodity_type_code,
            NEW.is_pedigree_ind,
            NEW.harvested_acres,
            NEW.harvested_acres_override,
            NEW.stored_yield_default_unit,
            NEW.sold_yield_default_unit,
            NEW.production_guarantee,
            NEW.harvested_yield,
            NEW.harvested_yield_override,
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

CREATE TRIGGER trg_verified_yield_contract_commodity_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.verified_yield_contract_commodity
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_verified_yield_contract_commodity_audit();