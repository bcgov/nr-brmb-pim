INSERT INTO cuws.declared_yield_rollup_forage(
	declared_yield_rollup_forage_guid, 
	declared_yield_contract_guid, 
	commodity_type_code, 
	total_field_acres, 
	total_bales_loads, 
	harvested_acres, 
	quantity_harvested_tons, 
	yield_per_acre, 
	create_user, 
	create_date, 
	update_user, 
	update_date
	)
SELECT replace(cast(gen_random_uuid() as text), '-', ''), 
	declared_yield_contract_guid, 
	commodity_type_code, 
	total_field_acres, 
	(
		SELECT SUM(dyff.total_bales_loads) AS total_bales
		FROM declared_yield_contract dyc
		JOIN grower_contract_year gcy ON gcy.contract_id = dyc.contract_id AND gcy.crop_year = dyc.crop_year
		JOIN contracted_field_detail cfd ON cfd.grower_contract_year_id = gcy.grower_contract_year_id
		JOIN annual_field_detail afd ON afd.annual_field_detail_id = cfd.annual_field_detail_id
		JOIN inventory_field inv ON inv.field_id = afd.field_id AND inv.crop_year = afd.crop_year
		JOIN inventory_seeded_forage isd ON isd.inventory_field_guid = inv.inventory_field_guid
		JOIN declared_yield_field_forage dyff ON dyff.inventory_field_guid = inv.inventory_field_guid
		WHERE dyc.declared_yield_contract_guid = dyccf.declared_yield_contract_guid
		AND isd.commodity_type_code = dyccf.commodity_type_code
		GROUP BY inv.crop_year, isd.commodity_type_code
	) AS total_bales,
	harvested_acres, 
	quantity_harvested_tons, 
	yield_per_acre, 
	create_user, 
	create_date, 
	update_user, 
	update_date
	FROM cuws.declared_yield_contract_cmdty_forage dyccf
	WHERE NOT EXISTS (SELECT NULL FROM declared_yield_rollup_forage dyrf
					  WHERE dyccf.declared_yield_contract_guid = dyrf.declared_yield_contract_guid
					  AND dyccf.commodity_type_code = dyrf.commodity_type_code);
