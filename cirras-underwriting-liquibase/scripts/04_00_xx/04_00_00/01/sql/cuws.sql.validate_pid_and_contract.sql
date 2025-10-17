select *
from (select t.property_identifier
      from (select bs.property_identifier,
                   bs.contract_number,
                   bs.crop_year
            from berries_2026_staging bs
            where trim(from bs.property_identifier) != ''
            group by bs.property_identifier,
                     bs.contract_number,
                     bs.crop_year
            ) t
      group by t.property_identifier
      having count(*) > 1
      ) v
order by v.property_identifier;
