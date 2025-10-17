select *
from (select t.contract_number,
             t.crop_year,
             t.field_location
      from (select bs.contract_number,
                   bs.crop_year,
                   bs.field_location,
                   bs.property_identifier
            from berries_2026_staging bs
            where trim(from bs.field_location) != ''
            group by bs.contract_number,
                     bs.crop_year,
                     bs.field_location,
                     bs.property_identifier
            ) t
      group by t.contract_number,
               t.crop_year,
               t.field_location
      having count(*) > 1
      ) v
order by v.contract_number, v.crop_year, v.field_location;
