CREATE OR REPLACE FUNCTION cuws.fn_legal_land_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO cuws.legal_land_audit (
            legal_land_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            primary_property_identifier,
            primary_land_identifier_type_code,
            primary_reference_type_code,
            legal_description,
            legal_short_description,
            other_description,
            total_acres,
            active_from_crop_year,
            active_to_crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.lla_seq'),
            'INSERT',
            CURRENT_TIMESTAMP,
            NEW.legal_land_id,
            NEW.primary_property_identifier,
            NEW.primary_land_identifier_type_code,
            NEW.primary_reference_type_code,
            NEW.legal_description,
            NEW.legal_short_description,
            NEW.other_description,
            NEW.total_acres,
            NEW.active_from_crop_year,
            NEW.active_to_crop_year,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.legal_land_audit (
            legal_land_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            primary_property_identifier,
            primary_land_identifier_type_code,
            primary_reference_type_code,
            legal_description,
            legal_short_description,
            other_description,
            total_acres,
            active_from_crop_year,
            active_to_crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.lla_seq'),
            'UPDATE',
            CURRENT_TIMESTAMP,
            NEW.legal_land_id,
            NEW.primary_property_identifier,
            NEW.primary_land_identifier_type_code,
            NEW.primary_reference_type_code,
            NEW.legal_description,
            NEW.legal_short_description,
            NEW.other_description,
            NEW.total_acres,
            NEW.active_from_crop_year,
            NEW.active_to_crop_year,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;

    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.legal_land_audit (
            legal_land_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            legal_land_id,
            primary_property_identifier,
            primary_land_identifier_type_code,
            primary_reference_type_code,
            legal_description,
            legal_short_description,
            other_description,
            total_acres,
            active_from_crop_year,
            active_to_crop_year,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.lla_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.legal_land_id,
            OLD.primary_property_identifier,
            OLD.primary_land_identifier_type_code,
            OLD.primary_reference_type_code,
            OLD.legal_description,
            OLD.legal_short_description,
            OLD.other_description,
            OLD.total_acres,
            OLD.active_from_crop_year,
            OLD.active_to_crop_year,
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

CREATE TRIGGER trg_legal_land_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.legal_land
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_legal_land_audit();
