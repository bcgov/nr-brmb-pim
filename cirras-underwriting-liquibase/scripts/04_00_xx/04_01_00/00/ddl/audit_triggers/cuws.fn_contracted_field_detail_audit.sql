CREATE OR REPLACE FUNCTION cuws.fn_contracted_field_detail_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'INSERT') THEN
        INSERT INTO cuws.contracted_field_detail_audit (
            contracted_field_detail_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            contracted_field_detail_id,
            annual_field_detail_id,
            grower_contract_year_id,
            display_order,
            is_leased_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.cfda_seq'),
            'INSERT',
            CURRENT_TIMESTAMP,
            NEW.contracted_field_detail_id,
            NEW.annual_field_detail_id,
            NEW.grower_contract_year_id,
            NEW.display_order,
            NEW.is_leased_ind,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.contracted_field_detail_audit (
            contracted_field_detail_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            contracted_field_detail_id,
            annual_field_detail_id,
            grower_contract_year_id,
            display_order,
            is_leased_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.cfda_seq'),
            'UPDATE',
            CURRENT_TIMESTAMP,
            NEW.contracted_field_detail_id,
            NEW.annual_field_detail_id,
            NEW.grower_contract_year_id,
            NEW.display_order,
            NEW.is_leased_ind,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;

    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.contracted_field_detail_audit (
            contracted_field_detail_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            contracted_field_detail_id,
            annual_field_detail_id,
            grower_contract_year_id,
            display_order,
            is_leased_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.cfda_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.contracted_field_detail_id,
            OLD.annual_field_detail_id,
            OLD.grower_contract_year_id,
            OLD.display_order,
            OLD.is_leased_ind,
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

CREATE TRIGGER trg_contracted_field_detail_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.contracted_field_detail
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_contracted_field_detail_audit();
