CREATE OR REPLACE FUNCTION cuws.fn_declared_yield_contract_commodity_berries_ob()
RETURNS TRIGGER AS $$
DECLARE
 v_dyccb_guid varchar(32);
 v_userid varchar(32);
BEGIN
	IF (TG_OP = 'DELETE') THEN
		v_dyccb_guid := OLD.declared_yield_contract_commodity_berries_guid;
		v_userid := OLD.update_user;
	ELSIF (TG_OP = 'UPDATE' OR TG_OP = 'INSERT') THEN
		v_dyccb_guid := NEW.declared_yield_contract_commodity_berries_guid;
		v_userid := NEW.update_user;
    END IF;
	
	INSERT INTO cuws.declared_yield_contract_commodity_berries_ob (
		dyccb_ob_id,
		audit_transaction_type_code,
		declared_yield_contract_commodity_berries_guid,
		create_user,
		create_date,
		update_user,
		update_date
	)
	VALUES (
		nextval('cuws.dyccbo_seq'),
		TG_OP,
		v_dyccb_guid,
		v_userid,
		current_timestamp,
		v_userid,
		current_timestamp
	);

    IF (TG_OP = 'DELETE') THEN
        RETURN OLD;
    END IF;
    RETURN NEW;
	
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_declared_yield_contract_commodity_berries_ob
AFTER INSERT OR UPDATE OR DELETE
ON cuws.declared_yield_contract_commodity_berries
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_declared_yield_contract_commodity_berries_ob();