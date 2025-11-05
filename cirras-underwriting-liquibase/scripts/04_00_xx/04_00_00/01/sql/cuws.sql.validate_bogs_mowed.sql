-- Cranberry Plantings with a non-date value for Bogs Mowed.

select *
from berries_2026_staging bs
where trim(from bs.bogs_mowed) <> ''
  and bs.bogs_mowed !~ '^\d\d\d\d-\d\d-\d\d$'
order by bs.input_line_number;
