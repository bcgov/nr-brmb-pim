-- Plantings without a Property Identifier.

select *
from berries_2026_staging bs
where trim(from bs.property_identifier) = ''
order by bs.input_line_number;
