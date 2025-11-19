-- Cranberry Plantings with a non-date value for Bogs Renovated.

select *
from berries_2026_staging bs
where trim(from bs.bogs_renovated) <> ''
  and bs.bogs_renovated !~ '^\d\d\d\d-\d\d-\d\d$'
order by bs.input_line_number;
