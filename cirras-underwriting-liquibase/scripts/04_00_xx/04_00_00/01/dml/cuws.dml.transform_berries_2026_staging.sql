\qecho 'Transform berries_2026_staging'

-- Policy:

\qecho 'contract_id and crop_year: Lookup from policy'
update berries_2026_staging as bs 
set contract_id = p.contract_id
from policy p 
where p.contract_number = bs.contract_number::varchar
  and p.crop_year = bs.crop_year;


\qecho 'grower_contract_year_id: Lookup from grower_contract_year'
update berries_2026_staging as bs 
set grower_contract_year_id = gcy.grower_contract_year_id
from grower_contract_year gcy 
where gcy.contract_id = bs.contract_Id
  and gcy.crop_year = bs.crop_year;

-- Field

\qecho 'field_bog_name: Trim whitespace. If empty, set to UNSPECIFIED'
update berries_2026_staging set field_bog_name = trim(from field_bog_name);
update berries_2026_staging set field_bog_name = 'UNSPECIFIED' where field_bog_name = '';

\qecho 'field_location: Trim whitespace. If empty, set to null'
update berries_2026_staging set field_location = trim(from field_location);
update berries_2026_staging set field_location = null where field_location = '';

\qecho 'property_identifier: Trim whitespace'
update berries_2026_staging set property_identifier = trim(from property_identifier);

\qecho 'primary_property_identifier: Set to first one'
update berries_2026_staging 
set primary_property_identifier = substring(property_identifier from '^\d\d\d-\d\d\d-\d\d\d')
where property_identifier <> '';

\qecho 'primary_land_identifier_type_code: Always PID'
update berries_2026_staging set primary_land_identifier_type_code = 'PID' where primary_property_identifier <> '';

\qecho 'secondary_property_identifiers: Remove first, and normalize separator'
update berries_2026_staging
set secondary_property_identifiers = substring(property_identifier from 12)
where primary_property_identifier <> ''
  and length(property_identifier) > 11;

update berries_2026_staging
set secondary_property_identifiers = regexp_replace(secondary_property_identifiers, '^[^\d-]+', '')
where secondary_property_identifiers is not null;

update berries_2026_staging
set secondary_property_identifiers = regexp_replace(secondary_property_identifiers, '[^\d-]+', ', ', 'g')
where secondary_property_identifiers is not null;


-- Plantings

\qecho 'crop_name: Trim whitespace, upper-case to match db names'
update berries_2026_staging set crop_name = upper(trim(from crop_name));

\qecho 'crop_commodity_id: Lookup using crop_name'
update berries_2026_staging t
set crop_commodity_id = cc.crop_commodity_id
from crop_commodity cc
where t.crop_name <> ''
  and cc.commodity_name = t.crop_name
  and cc.insurance_plan_id = 3;

\qecho 'variety_name: Trim whitespace, upper-case to match db names'
update berries_2026_staging set variety_name = upper(trim(from variety_name));

\qecho 'variety_name: Replace empty with null'
update berries_2026_staging set variety_name = null where variety_name = '';

\qecho 'variety_name: Fix Variety names to match db names'
update berries_2026_staging set variety_name = 'HARDYBLUE (1613)' where variety_name = 'HARDIBLUE' and crop_name = 'BLUEBERRY';
update berries_2026_staging set variety_name = 'TOPSHELF' where variety_name = 'TOP SHELF' and crop_name = 'BLUEBERRY';
update berries_2026_staging set variety_name = 'TILAMOOK' where variety_name = 'TILLAMOOK' and crop_name = 'STRAWBERRY';
update berries_2026_staging set variety_name = 'GRYGLESKI' where variety_name = 'GRYLESKI' and crop_name = 'CRANBERRY';
update berries_2026_staging set variety_name = 'HAINES' where variety_name = 'HAYNES' and crop_name = 'CRANBERRY';
update berries_2026_staging set variety_name = 'PILGRIM' where variety_name = 'PILGRAM' and crop_name = 'CRANBERRY';


\qecho 'crop_variety_id: Lookup using variety_name'
update berries_2026_staging t
set crop_variety_id = cv.crop_variety_id
from crop_variety cv
where t.variety_name is not null
  and cv.variety_name = t.variety_name
  and cv.crop_commodity_id = t.crop_commodity_id
  and cv.effective_date <= now()
  and cv.expiry_date >= now();

\qecho 'bog_id: Trim whitespace'
update berries_2026_staging set bog_id = trim(from bog_id);
update berries_2026_staging set bog_id = null where bog_id = '';

\qecho 'year_planted: Trim whitespace'
update berries_2026_staging set year_planted = trim(from year_planted);
update berries_2026_staging set year_planted = null where year_planted = '';

\qecho 'acres: Trim whitespace'
update berries_2026_staging set acres = trim(from acres);
update berries_2026_staging set acres = null where acres = '';

\qecho 'ownership: Trim whitespace, upper-case, default to OWNED'
update berries_2026_staging set ownership = upper(trim(from ownership));
update berries_2026_staging set ownership = 'OWNED' where ownership = '';

\qecho 'row_spacing, plant_spacing: Trim whitespace'
update berries_2026_staging set row_spacing = trim(from row_spacing), plant_spacing = trim(from plant_spacing);
update berries_2026_staging set row_spacing = null where row_spacing = '';
update berries_2026_staging set plant_spacing = null where plant_spacing = '';

\qecho 'bogs_mowed, bogs_renovated: Trim whitespace'
update berries_2026_staging set bogs_mowed = trim(from bogs_mowed), bogs_renovated = trim(from bogs_renovated);
update berries_2026_staging set bogs_mowed = null where bogs_mowed = '';
update berries_2026_staging set bogs_renovated = null where bogs_renovated = '';

\qecho 'harvested_ind: Trim whitespace, default to N'
update berries_2026_staging set harvested_ind = trim(from harvested_ind);
update berries_2026_staging set harvested_ind = 'N' where harvested_ind = '';


\qecho 'is_quantity_insurable_ind, is_plant_insurable_ind, plant_insurability_type_code: Default to N or null'
update berries_2026_staging t
set is_quantity_insurable_ind = 'N',
    is_plant_insurable_ind = 'N',
    plant_insurability_type_code = null
;


\echo 'is_quantity_insurable_ind, is_plant_insurable_ind, plant_insurability_type_code: Set to Y for eligible plantings'
update berries_2026_staging t
set is_quantity_insurable_ind = case when year_planted::numeric <= (crop_year - 2) then 'Y' else 'N' end,
    is_plant_insurable_ind = case when crop_name = 'BLUEBERRY' then 'Y'
                                  when crop_name = 'STRAWBERRY' and year_planted::numeric in (crop_year - 1, crop_year - 2) then 'Y'
                                  else 'N'
                             end,
    plant_insurability_type_code = case when crop_name = 'STRAWBERRY' and year_planted::numeric = (crop_year - 1) then 'ST1'
                                        when crop_name = 'STRAWBERRY' and year_planted::numeric = (crop_year - 2) then 'ST2'
                                        else null
                                   end
where year_planted is not null
  and acres is not null
  and variety_name is not null
;


\qecho 'planting_number: Auto-number for each field starting at 1'
with bs as (
        select v.input_line_number, row_number() OVER (PARTITION BY v.contract_number,
                                                                    v.crop_year,
                                                                    v.field_location,	
                                                                    v.field_bog_name, 
                                                                    v.primary_property_identifier,
                                                                    v.primary_land_identifier_type_code
                                                                 ORDER BY v.input_line_number
                                                      ) as rn
        from berries_2026_staging v
)
update berries_2026_staging t
set planting_number = (select bs.rn 
                       from bs
                       where bs.input_line_number = t.input_line_number
                      );

