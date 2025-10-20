-- Field Names within a Contract with different Locations.

select *
from (select t.contract_number,
             t.crop_year,
             t.field_bog_name
      from (select bs.contract_number,
                   bs.crop_year,
                   bs.field_bog_name,
                   bs.field_location
            from berries_2026_staging bs
            group by bs.contract_number,
                     bs.crop_year,
                     bs.field_bog_name,
                     bs.field_location
            ) t
      group by t.contract_number,
               t.crop_year,
               t.field_bog_name
      having count(*) > 1
      ) v
order by v.contract_number, v.crop_year, v.field_bog_name;
