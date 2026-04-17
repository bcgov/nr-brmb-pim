CREATE OR REPLACE FUNCTION cuws.fn_inventory_contract_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.inventory_contract_audit (
            inventory_contract_audit_id, audit_transaction_type_code, audit_time_stamp,
            inventory_contract_guid, contract_id, crop_year, unseeded_intentions_submitted_ind,
            seeded_crop_report_submitted_ind, fertilizer_ind, herbicide_ind, tilliage_ind,
            other_changes_ind, other_changes_comment, grain_from_prev_year_ind,
            inv_update_timestamp, inv_update_user, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('cuws.icoa_seq'), 'DELETE', CURRENT_TIMESTAMP,
            OLD.inventory_contract_guid, OLD.contract_id, OLD.crop_year, OLD.unseeded_intentions_submitted_ind,
            OLD.seeded_crop_report_submitted_ind, OLD.fertilizer_ind, OLD.herbicide_ind, OLD.tilliage_ind,
            OLD.other_changes_ind, OLD.other_changes_comment, OLD.grain_from_prev_year_ind,
            OLD.inv_update_timestamp, OLD.inv_update_user, OLD.create_user, OLD.create_date, OLD.update_user, OLD.update_date
        );
        RETURN OLD;
		
	ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.inventory_contract_audit (
            inventory_contract_audit_id, audit_transaction_type_code, audit_time_stamp,
            inventory_contract_guid, contract_id, crop_year, unseeded_intentions_submitted_ind,
            seeded_crop_report_submitted_ind, fertilizer_ind, herbicide_ind, tilliage_ind,
            other_changes_ind, other_changes_comment, grain_from_prev_year_ind,
            inv_update_timestamp, inv_update_user, create_user, create_date, update_user, update_date
        )
        VALUES (
            nextval('cuws.icoa_seq'), TG_OP, CURRENT_TIMESTAMP,
            NEW.inventory_contract_guid, NEW.contract_id, NEW.crop_year, NEW.unseeded_intentions_submitted_ind,
            NEW.seeded_crop_report_submitted_ind, NEW.fertilizer_ind, NEW.herbicide_ind, NEW.tilliage_ind,
            NEW.other_changes_ind, NEW.other_changes_comment, NEW.grain_from_prev_year_ind,
            NEW.inv_update_timestamp, NEW.inv_update_user, NEW.create_user, NEW.create_date, NEW.update_user, NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_contract_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_contract
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_contract_audit();