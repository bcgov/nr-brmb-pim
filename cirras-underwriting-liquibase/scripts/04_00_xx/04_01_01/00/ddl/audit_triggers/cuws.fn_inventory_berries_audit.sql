CREATE OR REPLACE FUNCTION cuws.fn_inventory_berries_audit()
RETURNS TRIGGER AS $$
BEGIN
	IF (TG_OP = 'DELETE') THEN
        INSERT INTO cuws.inventory_berries_audit (
            inventory_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_berries_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            plant_insurability_type_code,
            planted_year,
            planted_acres,
            mature_equivalent_acres,
            row_spacing,
            plant_spacing,
            total_plants,
            is_quantity_insurable_ind,
            is_plant_insurable_ind,
            bog_id,
            bog_mowed_date,
            bog_renovated_date,
            is_harvested_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.iba_seq'),
            'DELETE',
            CURRENT_TIMESTAMP,
            OLD.inventory_berries_guid,
            OLD.inventory_field_guid,
            OLD.crop_commodity_id,
            OLD.crop_variety_id,
            OLD.plant_insurability_type_code,
            OLD.planted_year,
            OLD.planted_acres,
            OLD.mature_equivalent_acres,
            OLD.row_spacing,
            OLD.plant_spacing,
            OLD.total_plants,
            OLD.is_quantity_insurable_ind,
            OLD.is_plant_insurable_ind,
            OLD.bog_id,
            OLD.bog_mowed_date,
            OLD.bog_renovated_date,
            OLD.is_harvested_ind,
            OLD.create_user,
            OLD.create_date,
            OLD.update_user,
            OLD.update_date
        );
        RETURN OLD;
    ELSIF (TG_OP = 'INSERT' OR TG_OP = 'UPDATE') THEN
        INSERT INTO cuws.inventory_berries_audit (
            inventory_berries_audit_id,
            audit_transaction_type_code,
            audit_time_stamp,
            inventory_berries_guid,
            inventory_field_guid,
            crop_commodity_id,
            crop_variety_id,
            plant_insurability_type_code,
            planted_year,
            planted_acres,
            mature_equivalent_acres,
            row_spacing,
            plant_spacing,
            total_plants,
            is_quantity_insurable_ind,
            is_plant_insurable_ind,
            bog_id,
            bog_mowed_date,
            bog_renovated_date,
            is_harvested_ind,
            create_user,
            create_date,
            update_user,
            update_date
        )
        VALUES (
            nextval('cuws.iba_seq'),
            TG_OP,
            CURRENT_TIMESTAMP,
            NEW.inventory_berries_guid,
            NEW.inventory_field_guid,
            NEW.crop_commodity_id,
            NEW.crop_variety_id,
            NEW.plant_insurability_type_code,
            NEW.planted_year,
            NEW.planted_acres,
            NEW.mature_equivalent_acres,
            NEW.row_spacing,
            NEW.plant_spacing,
            NEW.total_plants,
            NEW.is_quantity_insurable_ind,
            NEW.is_plant_insurable_ind,
            NEW.bog_id,
            NEW.bog_mowed_date,
            NEW.bog_renovated_date,
            NEW.is_harvested_ind,
            NEW.create_user,
            NEW.create_date,
            NEW.update_user,
            NEW.update_date
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_inventory_berries_audit
AFTER INSERT OR UPDATE OR DELETE
ON cuws.inventory_berries
FOR EACH ROW
EXECUTE FUNCTION cuws.fn_inventory_berries_audit();

