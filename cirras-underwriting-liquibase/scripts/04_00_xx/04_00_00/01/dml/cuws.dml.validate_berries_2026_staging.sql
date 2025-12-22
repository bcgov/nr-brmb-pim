\qecho 'Validate berries_2026_staging'
\qecho 'IMPORTANT:'
\qecho 'Review the result of each validation query. If test_result=PASS for all, then validation has passed.'
\qecho 'If test_result=FAIL for any, then validation has failed. Rollback the current transaction and review the error.'

\qecho 'policy_number: Contains a valid 2026 BERRIES Policy:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
left join policy p on p.contract_number = bs.contract_number::varchar
                  and p.crop_year = bs.crop_year
where p.policy_number is null
   or p.insurance_plan_id <> 3
   or p.crop_year <> 2026;

\qecho 'contract_id, grower_contract_year_id: Populated:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.contract_id is null
   or bs.grower_contract_year_id is null;

\qecho 'field_name: Not empty, and no more than 28 chars:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.field_bog_name is null
   or bs.field_bog_name = ''
   or char_length(bs.field_bog_name) > 28;

\qecho 'field_location: No more than 128 chars:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.field_location is not null
   and char_length(bs.field_location) > 128;


\qecho 'primary_land_identifier_type_code: Not empty:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.primary_land_identifier_type_code is null
   or bs.primary_land_identifier_type_code = ''
   or bs.primary_land_identifier_type_code not in ('PID');

\qecho 'primary_property_identifier: Not empty, and correct format:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.primary_property_identifier is null
   or bs.primary_property_identifier = ''
   or bs.primary_property_identifier !~ '^\d\d\d-\d\d\d-\d\d\d$';


\qecho 'planting_number: Not empty:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.planting_number is null;

\qecho 'crop_variety_id, crop_commodity_id: Populated:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where (bs.variety_name is not null and bs.crop_variety_id is null)
   or bs.crop_commodity_id is null;

\qecho 'acres: At most 2 decimal precision:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where bs.acres is not null 
  and round(bs.acres::numeric, 2) <> bs.acres::numeric;

\qecho 'ownership: OWNED or LEASED'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
            end as test_result,
            count(*) as error_count
from berries_2026_staging bs
where bs.ownership is null
   or bs.ownership not in ('OWNED', 'LEASED');

\qecho 'ownership: Not mixed within a field'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
            end as test_result,
            count(*) as error_count
from (select v.contract_number, 
             v.crop_year, 
             v.field_bog_name, 
             v.field_location, 
             v.primary_property_identifier, 
             v.primary_land_identifier_type_code 
      from (select distinct t.contract_number, 
                            t.crop_year, 
                            t.field_bog_name, 
                            t.field_location, 
                            t.primary_property_identifier, 
                            t.primary_land_identifier_type_code, 
                            t.ownership 
            from berries_2026_staging t
            where t.year_planted is not null
              and t.acres is not null
              and t.variety_name is not null
           ) v 
      group by v.contract_number, 
               v.crop_year, 
               v.field_bog_name, 
               v.field_location, 
               v.primary_property_identifier, 
               v.primary_land_identifier_type_code 
      having count(*) > 1
     ) u;

\qecho 'harvested_ind, is_quantity_insurable_ind, is_plant_insurable_ind: Populated:'
select case when count(*) = 0 then 'PASS'
            else 'FAIL'
	   end as test_result,
	   count(*) as error_count
from berries_2026_staging bs
where (bs.harvested_ind is null or bs.harvested_ind not in ('Y', 'N'))
   or (bs.is_quantity_insurable_ind is null or bs.is_quantity_insurable_ind not in ('Y', 'N'))
   or (bs.is_plant_insurable_ind is null or bs.is_plant_insurable_ind not in ('Y', 'N'));


\qecho 'All validation queries have been run. If test_result=PASS for all, then validation has passed.'
\qecho 'If test_result=FAIL for any, then validation has failed. Rollback the current transaction and review the error.'

-- Display same message in console.
\echo 'All validation queries have been run. If test_result=PASS for all, then validation has passed.'
\echo 'If test_result=FAIL for any, then validation has failed. Rollback the current transaction and review the error.'
