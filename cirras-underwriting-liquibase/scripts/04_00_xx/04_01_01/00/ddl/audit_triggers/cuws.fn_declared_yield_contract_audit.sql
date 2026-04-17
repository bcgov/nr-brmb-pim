CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_contract_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_contract_audit (
            declared_yield_contract_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_contract_guid,
            contract_id,
            crop_year,
            declaration_of_production_date,
            dop_update_timestamp,
            dop_update_user,
            entered_yield_meas_unit_type_code,
            default_yield_meas_unit_type_code,
            grain_from_other_source_ind,
            baler_wagon_info,
            total_livestock,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyca_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_contract_guid,
            OLD.contract_id,
            OLD.crop_year,
            OLD.declaration_of_production_date,
            OLD.dop_update_timestamp,
            OLD.dop_update_user,
            OLD.entered_yield_meas_unit_type_code,
            OLD.default_yield_meas_unit_type_code,
            OLD.grain_from_other_source_ind,
            OLD.baler_wagon_info,
            OLD.total_livestock,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;	
	ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.declared_yield_contract_audit (
            declared_yield_contract_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_contract_guid,
            contract_id,
            crop_year,
            declaration_of_production_date,
            dop_update_timestamp,
            dop_update_user,
            entered_yield_meas_unit_type_code,
            default_yield_meas_unit_type_code,
            grain_from_other_source_ind,
            baler_wagon_info,
            total_livestock,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dyca_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_contract_guid,
            NEW.contract_id,
            NEW.crop_year,
            NEW.declaration_of_production_date,
            NEW.dop_update_timestamp,
            NEW.dop_update_user,
            NEW.entered_yield_meas_unit_type_code,
            NEW.default_yield_meas_unit_type_code,
            NEW.grain_from_other_source_ind,
            NEW.baler_wagon_info,
            NEW.total_livestock,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_declared_yield_contract_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_contract
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_contract_audit();

