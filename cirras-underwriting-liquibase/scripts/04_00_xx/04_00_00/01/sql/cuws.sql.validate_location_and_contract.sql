select *
from (select t.field_location
      from (select bs.field_location,
                   bs.contract_number,
                   bs.crop_year
            from berries_2026_staging bs
            where trim(from bs.field_location) != ''
            group by bs.field_location,
                     bs.contract_number,
                     bs.crop_year
            ) t
      group by t.field_location
      having count(*) > 1
      ) v
order by v.field_location;
