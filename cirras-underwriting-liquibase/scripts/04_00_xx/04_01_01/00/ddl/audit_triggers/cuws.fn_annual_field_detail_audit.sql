CREATE OR REPLACE FUNCTION cuws.fn_annual_field_detail_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.annual_field_detail_audit (
            annual_field_detail_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            annual_field_detail_id,
            legal_land_id,
            field_id,
            crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.afda_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.annual_field_detail_id,
            OLD.legal_land_id,
            OLD.field_id,
            OLD.crop_year,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
	ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.annual_field_detail_audit (
            annual_field_detail_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            annual_field_detail_id,
            legal_land_id,
            field_id,
            crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.afda_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.annual_field_detail_id,
            NEW.legal_land_id,
            NEW.field_id,
            NEW.crop_year,
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

CREATE TRIGGER trg_annual_field_detail_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.annual_field_detail
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_annual_field_detail_audit();