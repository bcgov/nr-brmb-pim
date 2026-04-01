CREATE OR REPLACE FUNCTION cuws.fn_field_audit_log()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.field_audit (
            field_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            field_id,
            field_label,
            active_from_crop_year,
            active_to_crop_year,
            location,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.flda_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.field_id,
            OLD.field_label,
            OLD.active_from_crop_year,
            OLD.active_to_crop_year,
            OLD.location,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.field_audit (
            field_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            field_id,
            field_label,
            active_from_crop_year,
            active_to_crop_year,
            location,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.flda_seq'),
            TG_OP, -- This will be 'INSERT' or 'UPDATE'
            CURRENT_TIMESTAMP,
            NEW.field_id,
            NEW.field_label,
            NEW.active_from_crop_year,
            NEW.active_to_crop_year,
            NEW.location,
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

CREATE TRIGGER trg_field_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.field
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_field_audit_log();