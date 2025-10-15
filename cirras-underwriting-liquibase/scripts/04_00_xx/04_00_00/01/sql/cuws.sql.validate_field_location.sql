select *
from berries_2026_staging bs
where char_length(bs.field_location) > 128
order by input_line_number;
