-- Cranberry Plantings with a value for Bogs Renovated. Some of them are just Years, other are Year + Month.

select *
from berries_2026_staging bs
where trim(from bs.bogs_renovated) <> ''
order by bs.input_line_number;
