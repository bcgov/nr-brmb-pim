-- Plantings where Year Planted is empty, or not a valid value.

select *
from berries_2026_staging bs
where bs.year_planted !~ '^\d\d\d\d$'
order by bs.input_line_number;
