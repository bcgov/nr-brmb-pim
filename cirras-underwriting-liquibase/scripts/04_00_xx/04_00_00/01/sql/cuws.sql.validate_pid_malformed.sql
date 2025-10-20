-- Plantings whose Property Identifier is not in the expected format of XXX-XXX-XXX. Some of 
-- these contain multiple PIDs, one of which needs to be selected as Primary.

select *
from berries_2026_staging bs
where trim(from bs.property_identifier) != ''
  and trim(from bs.property_identifier) !~ '^\d\d\d-\d\d\d-\d\d\d$'
order by bs.input_line_number;
