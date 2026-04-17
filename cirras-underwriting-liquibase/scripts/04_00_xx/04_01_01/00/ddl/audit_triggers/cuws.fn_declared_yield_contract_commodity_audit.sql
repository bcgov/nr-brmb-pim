CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_contract_commodity_audit()
RETURNS TRIGGER AS $$
BEGIN
	IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.declared_yield_contract_commodity_audit (
            declared_yield_contract_commodity_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_contract_commodity_guid,
            declared_yield_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            harvested_acres,
            stored_yield,
            stored_yield_default_unit,
            sold_yield,
            sold_yield_default_unit,
            grade_modifier_type_code,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dycca_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.declared_yield_contract_commodity_guid,
            OLD.declared_yield_contract_guid,
            OLD.crop_commodity_id,
            OLD.is_pedigree_ind,
            OLD.harvested_acres,
            OLD.stored_yield,
            OLD.stored_yield_default_unit,
            OLD.sold_yield,
            OLD.sold_yield_default_unit,
            OLD.grade_modifier_type_code,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.declared_yield_contract_commodity_audit (
            declared_yield_contract_commodity_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            declared_yield_contract_commodity_guid,
            declared_yield_contract_guid,
            crop_commodity_id,
            is_pedigree_ind,
            harvested_acres,
            stored_yield,
            stored_yield_default_unit,
            sold_yield,
            sold_yield_default_unit,
            grade_modifier_type_code,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.dycca_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.declared_yield_contract_commodity_guid,
            NEW.declared_yield_contract_guid,
            NEW.crop_commodity_id,
            NEW.is_pedigree_ind,
            NEW.harvested_acres,
            NEW.stored_yield,
            NEW.stored_yield_default_unit,
            NEW.sold_yield,
            NEW.sold_yield_default_unit,
            NEW.grade_modifier_type_code,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_contract_commodity_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_contract_commodity
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_contract_commodity_audit();

