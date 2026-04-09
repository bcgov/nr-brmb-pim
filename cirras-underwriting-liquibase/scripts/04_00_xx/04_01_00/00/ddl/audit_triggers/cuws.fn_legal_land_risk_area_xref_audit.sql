CREATE OR REPLACE FUNCTION cuws.fn_legal_land_risk_area_xref_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.legal_land_risk_area_xref_audit (
            legal_land_risk_area_xref_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            risk_area_id,
            active_from_crop_year,
            active_to_crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.llraxa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.legal_land_id,
            OLD.risk_area_id,
            OLD.active_from_crop_year,
            OLD.active_to_crop_year,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.legal_land_risk_area_xref_audit (
            legal_land_risk_area_xref_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            risk_area_id,
            active_from_crop_year,
            active_to_crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.llraxa_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.legal_land_id,
            NEW.risk_area_id,
            NEW.active_from_crop_year,
            NEW.active_to_crop_year,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_legal_land_risk_area_xref_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.legal_land_risk_area_xref
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_legal_land_risk_area_xref_audit();
