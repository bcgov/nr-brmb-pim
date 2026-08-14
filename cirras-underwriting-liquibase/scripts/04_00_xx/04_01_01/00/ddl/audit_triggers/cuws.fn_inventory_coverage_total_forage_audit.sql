CREATE OR REPLACE FUNCTION cuws.fn_inventory_coverage_total_forage_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.inventory_coverage_total_forage_audit (
            inventory_coverage_total_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_coverage_total_forage_guid,
            inventory_contract_guid,
            crop_commodity_id,
            plant_insurability_type_code,
            is_unseeded_insurable_ind,
            total_field_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.ictfa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_coverage_total_forage_guid,
            OLD.inventory_contract_guid,
            OLD.crop_commodity_id,
            OLD.plant_insurability_type_code,
            OLD.is_unseeded_insurable_ind,
            OLD.total_field_acres,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
	ELSEIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_coverage_total_forage_audit (
            inventory_coverage_total_forage_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_coverage_total_forage_guid,
            inventory_contract_guid,
            crop_commodity_id,
            plant_insurability_type_code,
            is_unseeded_insurable_ind,
            total_field_acres,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.ictfa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_coverage_total_forage_guid,
            NEW.inventory_contract_guid,
            NEW.crop_commodity_id,
            NEW.plant_insurability_type_code,
            NEW.is_unseeded_insurable_ind,
            NEW.total_field_acres,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_coverage_total_forage_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_coverage_total_forage
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_coverage_total_forage_audit();
