CREATE OR REPLACE FUNCTION cuws.fn_legal_land_field_xref_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO cuws.legal_land_field_xref_audit (
            legal_land_field_xref_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            field_id,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.llfxa_seq'),
            'INSERT',
            CURRENT_TIMESTAMP,
            NEW.legal_land_id,
            NEW.field_id,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.legal_land_field_xref_audit (
            legal_land_field_xref_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            field_id,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.llfxa_seq'),
            'UPDATE',
            CURRENT_TIMESTAMP,
            NEW.legal_land_id,
            NEW.field_id,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;

    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.legal_land_field_xref_audit (
            legal_land_field_xref_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            field_id,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.llfxa_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.legal_land_id,
            OLD.field_id,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_legal_land_field_xref_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.legal_land_field_xref
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_legal_land_field_xref_audit();
