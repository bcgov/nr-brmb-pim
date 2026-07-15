CREATE OR REPLACE FUNCTION cuws.fn_underwriting_comment_audit()
RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.underwriting_comment_audit (
            underwriting_comment_audit_id,
			audit_transaction_type_code,
			audit_time_stamp,
			underwriting_comment_guid,
			underwriting_comment_type_code,
			annual_field_detail_id,
			grower_contract_year_id,
			declared_yield_contract_guid,
			verified_yield_summary_guid,
			underwriting_comment,
			is_forced_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.uca_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.underwriting_comment_guid,
            OLD.underwriting_comment_type_code,
            OLD.annual_field_detail_id,
            OLD.grower_contract_year_id,
            OLD.declared_yield_contract_guid,
			OLD.verified_yield_summary_guid,
			OLD.underwriting_comment,
			OLD.is_forced_ind,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
        INSERT INTO cuws.underwriting_comment_audit (
            underwriting_comment_audit_id,
			audit_transaction_type_code,
			audit_time_stamp,
			underwriting_comment_guid,
			underwriting_comment_type_code,
			annual_field_detail_id,
			grower_contract_year_id,
			declared_yield_contract_guid,
			verified_yield_summary_guid,
			underwriting_comment,
			is_forced_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.uca_seq'),
            TG_OP, -- This will be 'INSERT' or 'UPDATE'
            CURRENT_TIMESTAMP,
            NEW.underwriting_comment_guid,
            NEW.underwriting_comment_type_code,
            NEW.annual_field_detail_id,
            NEW.grower_contract_year_id,
            NEW.declared_yield_contract_guid,
			NEW.verified_yield_summary_guid,
			NEW.underwriting_comment,
			NEW.is_forced_ind,
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

CREATE TRIGGER trg_underwriting_comment_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.underwriting_comment
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_underwriting_comment_audit();