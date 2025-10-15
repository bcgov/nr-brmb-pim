select *
from berries_2026_staging bs
where char_length(bs.field_bog_name) > 28
order by input_line_number;
