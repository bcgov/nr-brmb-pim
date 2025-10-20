-- Cranberry Plantings with a value for Bogs Mowed. Most of them are not actualy dates.

select *
from berries_2026_staging bs
where trim(from bs.bogs_mowed) <> ''
order by bs.input_line_number;
