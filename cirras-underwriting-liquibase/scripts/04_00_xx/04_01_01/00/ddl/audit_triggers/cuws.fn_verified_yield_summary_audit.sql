CREATE OR REPLACE FUNCTION cuws.fn_verified_yield_summary_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.verified_yield_summary_audit (
            verified_yield_summary_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_summary_guid,
            verified_yield_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            production_acres,
            harvested_yield,
            harvested_yield_per_acre,
            appraised_yield,
            assessed_yield,
            yield_to_count,
            yield_percent_py,
            production_guarantee,
            probable_yield,
            insurable_value_hundred_percent,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vysa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.verified_yield_summary_guid,
            OLD.verified_yield_contract_guid,
            OLD.crop_commodity_id,
            OLD.is_pedigree_ind,
            OLD.production_acres,
            OLD.harvested_yield,
            OLD.harvested_yield_per_acre,
            OLD.appraised_yield,
            OLD.assessed_yield,
            OLD.yield_to_count,
            OLD.yield_percent_py,
            OLD.production_guarantee,
            OLD.probable_yield,
            OLD.insurable_value_hundred_percent,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSE
        -- Handles INSERT and UPDATE
        INSERT INTO cuws.verified_yield_summary_audit (
            verified_yield_summary_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            verified_yield_summary_guid,
            verified_yield_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            production_acres,
            harvested_yield,
            harvested_yield_per_acre,
            appraised_yield,
            assessed_yield,
            yield_to_count,
            yield_percent_py,
            production_guarantee,
            probable_yield,
            insurable_value_hundred_percent,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.vysa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.verified_yield_summary_guid,
            NEW.verified_yield_contract_guid,
            NEW.crop_commodity_id,
            NEW.is_pedigree_ind,
            NEW.production_acres,
            NEW.harvested_yield,
            NEW.harvested_yield_per_acre,
            NEW.appraised_yield,
            NEW.assessed_yield,
            NEW.yield_to_count,
            NEW.yield_percent_py,
            NEW.production_guarantee,
            NEW.probable_yield,
            NEW.insurable_value_hundred_percent,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_verified_yield_summary_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.verified_yield_summary
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_verified_yield_summary_audit();
