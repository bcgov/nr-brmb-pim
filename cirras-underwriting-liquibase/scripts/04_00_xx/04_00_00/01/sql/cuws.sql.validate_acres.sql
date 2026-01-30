-- Plantings where Acres is empty, or has more than 2 decimals.

select *
from berries_2026_staging bs
where (trim(from bs.acres) = '' 
       and (trim(from bs.row_spacing) <> '' or trim(from bs.plant_spacing) <> '' or trim(from bs.year_planted) <> '' 
            or trim(from bs.variety_name) <> '' or trim(from bs.harvested_ind) = 'Y')
       )
   or (trim(from bs.acres) <> '' and round(bs.acres::numeric, 2) <> bs.acres::numeric)
order by bs.input_line_number;
