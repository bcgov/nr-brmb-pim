-- Plantings with no variety, or whose variety does not exist in DEV.

select *
from berries_2026_staging bs
where (trim(from bs.variety_name) = ''
        and (trim(from bs.row_spacing) <> '' or trim(from bs.plant_spacing) <> '' or trim(from bs.year_planted) <> '' or trim(from bs.acres) <> '' or trim(from bs.harvested_ind) = 'Y')
      )
      or (trim(from bs.variety_name) <> ''
            and not exists (select 1
                           from crop_variety cv
                           join crop_commodity cc on cc.crop_commodity_id = cv.crop_commodity_id
                           where upper(trim(from cv.variety_name)) = upper(trim(from bs.variety_name))
                             and upper(trim(from cc.commodity_name)) = upper(trim(from bs.crop_name))
                           )
      )
order by bs.input_line_number;
