-- Plantings with no variety, or whose variety does not exist in DEV.

select *
from berries_2026_staging bs
left join crop_variety cv on upper(trim(from cv.variety_name)) = upper(trim(from bs.variety_name))
left join crop_commodity cc on upper(cc.commodity_name) = upper(trim(from bs.crop_name))
                           and cc.crop_commodity_id = cv.crop_commodity_id
where (trim(from bs.variety_name) = ''
        and (trim(from bs.row_spacing) <> '' or trim(from bs.plant_spacing) <> '' or trim(from bs.year_planted) <> '' or trim(from bs.acres) <> '' or trim(from bs.harvested_ind) = 'Y')
      )
      or (trim(from bs.variety_name) <> ''
            and (cv.crop_variety_id is null
             or cc.crop_commodity_id is null)
      )
order by bs.input_line_number;
