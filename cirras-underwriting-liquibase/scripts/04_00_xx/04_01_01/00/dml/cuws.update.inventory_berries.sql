UPDATE inventory_berries ib SET
mature_equivalent_acres = 
(
	SELECT (COALESCE(m.scale, 1) * ib.planted_acres) as MEA
	FROM inventory_field f 
	LEFT JOIN commodity_maturity_scale m ON m.plant_age = (f.crop_year - ib.planted_year) AND m.crop_commodity_id = ib.crop_commodity_id
	WHERE f.inventory_field_guid = ib.inventory_field_guid
)
WHERE ib.planted_year is not null 
AND ib.planted_acres is not null
AND ib.mature_equivalent_acres is null;