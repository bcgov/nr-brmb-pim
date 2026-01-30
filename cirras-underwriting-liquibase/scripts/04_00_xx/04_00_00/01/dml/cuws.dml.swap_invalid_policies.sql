\qecho 'Swap invalid policies with a single test policy'
\qecho 'Only to be run in non-PROD environments where some policies are missing'

\prompt 'Contract Number to change policies to: ' v_swap_contract_number

update berries_2026_staging t
set contract_number = :'v_swap_contract_number'
where not exists (select 1 from policy p where p.contract_number = t.contract_number::varchar and p.crop_year = t.crop_year);
