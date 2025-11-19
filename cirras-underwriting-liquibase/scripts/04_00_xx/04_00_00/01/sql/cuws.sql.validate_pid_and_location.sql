-- PIDs within a Contract with different Field Locations.

select *
from (select t.contract_number,
             t.crop_year,
             t.property_identifier
      from (select bs.contract_number,
                   bs.crop_year,
                   bs.property_identifier,
                   bs.field_location
            from berries_2026_staging bs
            where trim(from bs.property_identifier) != ''
            group by bs.contract_number,
                     bs.crop_year,
                     bs.property_identifier,
                     bs.field_location
            ) t
      group by t.contract_number,
               t.crop_year,
               t.property_identifier
      having count(*) > 1
      ) v
order by v.contract_number, v.crop_year, v.property_identifier;
