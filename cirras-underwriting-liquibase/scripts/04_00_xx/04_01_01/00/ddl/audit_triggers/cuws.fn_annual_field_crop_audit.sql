CREATE OR REPLACE FUNCTION cuws.fn_annual_field_crop_audit()
RETURNS TRIGGER AS $$
BEGIN
	IF (TG_OP = 'DELETE') THEN
		INSERT INTO cuws.annual_field_crop_audit (
            annual_field_crop_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            annual_field_crop_id,
            annual_field_detail_id,
            crop_commodity_id,
            data_sync_trans_date,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.afca_seq'),
            'DELETE',
            current_timestamp,
            OLD.annual_field_crop_id,
            OLD.annual_field_detail_id,
            OLD.crop_commodity_id,
            OLD.data_sync_trans_date,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
	ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN	
        INSERT INTO cuws.annual_field_crop_audit (
            annual_field_crop_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            annual_field_crop_id,
            annual_field_detail_id,
            crop_commodity_id,
            data_sync_trans_date,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.afca_seq'),
            TG_OP,  -- This will be 'INSERT' or 'UPDATE'
            current_timestamp,
            NEW.annual_field_crop_id,
            NEW.annual_field_detail_id,
            NEW.crop_commodity_id,
            NEW.data_sync_trans_date,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_annual_field_crop_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.annual_field_crop
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_annual_field_crop_audit();