select *
from berries_2026_staging bs
where btrim(bs.acres) = '' 
   or round(bs.acres::numeric, 2) <> bs.acres::numeric
order by bs.input_line_number;
