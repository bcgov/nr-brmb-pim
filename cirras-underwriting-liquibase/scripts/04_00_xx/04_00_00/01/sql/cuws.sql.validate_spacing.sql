-- Non-empty Blueberry Plantings with no Row Spacing or Plant Spacing,
-- or with Row Spacing that is not an integer.

select *
from berries_2026_staging bs
where upper(trim(from bs.crop_name)) = 'BLUEBERRY'
  and (
        (
          (trim(from bs.row_spacing) = '' or trim(from bs.plant_spacing) = '')
           and (trim(from bs.year_planted) <> '' or trim(from bs.acres) <> '' or trim(from bs.variety_name) <> '' or trim(from bs.harvested_ind) = 'Y')
        ) 
        or (trim(from bs.row_spacing) <> '' and round(bs.row_spacing::numeric, 0) <> bs.row_spacing::numeric)
      )
order by bs.input_line_number;
