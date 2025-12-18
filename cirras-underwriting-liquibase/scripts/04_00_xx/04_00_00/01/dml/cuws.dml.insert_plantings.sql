\qecho 'Insert inventory_contract'

insert into inventory_contract(
	inventory_contract_guid, 
	contract_id, 
	crop_year, 
	unseeded_intentions_submitted_ind, 
	seeded_crop_report_submitted_ind, 
	fertilizer_ind, 
	herbicide_ind, 
	tilliage_ind, 
	other_changes_ind, 
	other_changes_comment, 
	grain_from_prev_year_ind, 
	create_user, 
	create_date, 
	update_user, 
	update_date, 
	inv_update_timestamp, 
	inv_update_user
) select replace(cast(gen_random_uuid() as text), '-', ''),
         t.contract_id,
		 t.crop_year,
		 'N',
		 'N',
		 'N',
		 'N',
		 'N',
		 'N',
		 null,
		 'N',
		 'CUWS_04_00_00',
		 now(),
		 'CUWS_04_00_00',
		 now(),
		 now(),
		 'CUWS_04_00_00'
  from berries_2026_staging t
  group by t.contract_id, 
           t.crop_year;

\qecho 'Insert inventory_field'

insert into inventory_field (
	inventory_field_guid, 
	insurance_plan_id, 
	field_id, 
	last_year_crop_commodity_id, 
	crop_year, 
	planting_number, 
	create_user, 
	create_date, 
	update_user, 
	update_date, 
	is_hidden_on_printout_ind, 
	underseeded_crop_variety_id, 
	underseeded_acres, 
	underseeded_inventory_seeded_forage_guid
) select replace(cast(gen_random_uuid() as text), '-', ''),
         3,
		 t.field_id,
		 null,
		 t.crop_year,
		 t.planting_number,
		 'CUWS_04_00_00',
		 now(),
		 'CUWS_04_00_00',
		 now(),
		 'N',
		 null,
		 null,
		 null
  from berries_2026_staging t;

\qecho 'Insert inventory_berries'

insert into inventory_berries (
	inventory_berries_guid, 
	inventory_field_guid, 
	crop_commodity_id, 
	crop_variety_id, 
	planted_year, 
	planted_acres, 
	row_spacing, 
	plant_spacing, 
	total_plants, 
	is_quantity_insurable_ind, 
	is_plant_insurable_ind,
	is_harvested_ind,
	create_user, 
	create_date, 
	update_user, 
	update_date
) select replace(cast(gen_random_uuid() as text), '-', ''),
         invf.inventory_field_guid,
		 t.crop_commodity_id,
		 t.crop_variety_id,
		 t.year_planted::integer, 
		 t.acres::numeric, 
		 t.row_spacing::numeric,
		 t.plant_spacing::numeric, 
		 CASE
	        WHEN t.row_spacing::numeric > 0 AND t.plant_spacing::numeric > 0  THEN round((t.acres::numeric  * 43560.0) / (t.row_spacing::numeric * t.plant_spacing::numeric))
	        ELSE 0
	     END total_plants,
		 t.is_quantity_insurable_ind, 
		 t.is_plant_insurable_ind,
		 t.harvested_ind,
		 'CUWS_04_00_00',
		 now(),
		 'CUWS_04_00_00',
		 now()
  from berries_2026_staging t
  join inventory_field invf on invf.field_id = t.field_id 
                           and invf.crop_year = t.crop_year 
						   and invf.planting_number = t.planting_number;

\qecho 'Insert inventory_contract_commodity_berries'

INSERT INTO cuws.inventory_contract_commodity_berries(
	inventory_contract_commodity_berries_guid, 
	inventory_contract_guid, 
	crop_commodity_id, 
	total_insured_plants,
	total_uninsured_plants, 
	total_quantity_insured_acres, 
	total_quantity_uninsured_acres,
	total_plant_insured_acres, 
	total_plant_uninsured_acres, 
	create_user, 
	create_date, 
	update_user, 
	update_date ) 
select replace(cast(gen_random_uuid() as text), '-', ''),
		ic.inventory_contract_guid,
		 ib.crop_commodity_id,
		  -- total_insured_plants
		 COALESCE((select sum(ib2.total_plants)
		 from inventory_berries ib2 
		 join inventory_field invf2 on ib2.inventory_field_guid = invf2.inventory_field_guid
		 join berries_2026_staging t2 on invf2.field_id = t2.field_id
                           				and invf2.crop_year = t2.crop_year
						   				and invf2.planting_number = t2.planting_number
		 join inventory_contract ic2 on ic2.contract_id = t2.contract_id
                            			and ic2.crop_year = t2.crop_year
		 where ic2.inventory_contract_guid = ic.inventory_contract_guid
		 and ib2.crop_commodity_id = ib.crop_commodity_id
		 and ib2.is_plant_insurable_ind = 'Y'
		 and ib2.total_plants > 0) , 0),
		 
		-- total_uninsured_plants
		 COALESCE((select sum(ib2.total_plants)
		 from inventory_berries ib2 
		 join inventory_field invf2 on ib2.inventory_field_guid = invf2.inventory_field_guid
		 join berries_2026_staging t2 on invf2.field_id = t2.field_id
                           				and invf2.crop_year = t2.crop_year
						   				and invf2.planting_number = t2.planting_number
		 join inventory_contract ic2 on ic2.contract_id = t2.contract_id
                            			and ic2.crop_year = t2.crop_year
		 where ic2.inventory_contract_guid = ic.inventory_contract_guid
		 and ib2.crop_commodity_id = ib.crop_commodity_id
		 and ib2.is_plant_insurable_ind = 'N'
		 and ib2.total_plants > 0) , 0) ,

		 -- total_quantity_insured_acres
		 COALESCE((select sum(ib2.planted_acres)
		 from inventory_berries ib2 
		 join inventory_field invf2 on ib2.inventory_field_guid = invf2.inventory_field_guid
		 join berries_2026_staging t2 on invf2.field_id = t2.field_id
                           				and invf2.crop_year = t2.crop_year
						   				and invf2.planting_number = t2.planting_number
		 join inventory_contract ic2 on ic2.contract_id = t2.contract_id
                            and ic2.crop_year = t2.crop_year
		 where ic2.inventory_contract_guid = ic.inventory_contract_guid
		 and ib2.crop_commodity_id = ib.crop_commodity_id
		 and ib2.is_quantity_insurable_ind = 'Y'
		 and ib2.planted_acres > 0), 0),

		 -- total_quantity_uninsured_acres
		 COALESCE((select sum(ib2.planted_acres)
		 from inventory_berries ib2 
		 join inventory_field invf2 on ib2.inventory_field_guid = invf2.inventory_field_guid
		 join berries_2026_staging t2 on invf2.field_id = t2.field_id
                           				and invf2.crop_year = t2.crop_year
						   				and invf2.planting_number = t2.planting_number
		 join inventory_contract ic2 on ic2.contract_id = t2.contract_id
                            			and ic2.crop_year = t2.crop_year
		 where ic2.inventory_contract_guid = ic.inventory_contract_guid
		 and ib2.crop_commodity_id = ib.crop_commodity_id
		 and ib2.is_quantity_insurable_ind = 'N'
		 and ib2.planted_acres > 0) ,0),

		  -- total_plant_insured_acres
		 COALESCE((select sum(ib2.planted_acres)
		 from inventory_berries ib2 
		 join inventory_field invf2 on ib2.inventory_field_guid = invf2.inventory_field_guid
		 join berries_2026_staging t2 on invf2.field_id = t2.field_id
                           				and invf2.crop_year = t2.crop_year
						   				and invf2.planting_number = t2.planting_number
		 join inventory_contract ic2 on ic2.contract_id = t2.contract_id
                            			and ic2.crop_year = t2.crop_year
		 where ic2.inventory_contract_guid = ic.inventory_contract_guid
		 and ib2.crop_commodity_id = ib.crop_commodity_id
		 and ib2.is_plant_insurable_ind = 'Y'
		 and ib2.planted_acres > 0), 0),

		  -- total_plant_uninsured_acres
		 COALESCE((select sum(ib2.planted_acres)
		 from inventory_berries ib2 
		 join inventory_field invf2 on ib2.inventory_field_guid = invf2.inventory_field_guid
		 join berries_2026_staging t2 on invf2.field_id = t2.field_id
                           				and invf2.crop_year = t2.crop_year
						   				and invf2.planting_number = t2.planting_number
		 join inventory_contract ic2 on ic2.contract_id = t2.contract_id
                            			and ic2.crop_year = t2.crop_year
		 where ic2.inventory_contract_guid = ic.inventory_contract_guid
		 and ib2.crop_commodity_id = ib.crop_commodity_id
		 and ib2.is_plant_insurable_ind = 'N'
		 and ib2.planted_acres > 0), 0),
		 'CUWS_04_00_00',
		 now(),
		 'CUWS_04_00_00',
		 now()
  from berries_2026_staging t
  join inventory_contract ic on ic.contract_id = t.contract_id
                            and ic.crop_year = t.crop_year
  join inventory_field invf on invf.field_id = t.field_id
                           and invf.crop_year = t.crop_year
						   and invf.planting_number = t.planting_number
  join inventory_berries ib on ib.inventory_field_guid = invf.inventory_field_guid
  group by ic.inventory_contract_guid,
           ib.crop_commodity_id;
