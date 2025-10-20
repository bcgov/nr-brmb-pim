-- Blueberry Plantings with no Row Spacing or Plant Spacing.

select *
from berries_2026_staging bs
where upper(trim(from bs.crop_name)) = 'BLUEBERRY'
  and (trim(from bs.row_spacing) = '' or trim(from bs.plant_spacing) = '')
order by bs.input_line_number;
