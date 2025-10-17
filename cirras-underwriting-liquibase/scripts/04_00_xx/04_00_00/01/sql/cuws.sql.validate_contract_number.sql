select * 
from berries_2026_staging bs
left join policy p on p.contract_number = bs.contract_number::varchar
where p.policy_number is null
   or p.insurance_plan_id <> 3
order by input_line_number;
