-- Plantings where Year Planted is empty, or not a valid value.

select *
from berries_2026_staging bs
where (trim(from bs.year_planted) = ''
        and (trim(from bs.row_spacing) <> '' or trim(from bs.plant_spacing) <> '' or trim(from bs.acres) <> '' or trim(from bs.variety_name) <> '' or trim(from bs.harvested_ind) = 'Y')
      )
   or (trim(from bs.year_planted) <> '' and bs.year_planted !~ '^\d\d\d\d$')
order by bs.input_line_number;
