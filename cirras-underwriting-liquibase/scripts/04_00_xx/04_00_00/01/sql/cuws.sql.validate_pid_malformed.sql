select *
from berries_2026_staging bs
where trim(from bs.property_identifier) != ''
  and trim(from bs.property_identifier) !~ '^\d\d\d-\d\d\d-\d\d\d$'
order by bs.input_line_number;
