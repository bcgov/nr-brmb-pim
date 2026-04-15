UPDATE cuws.inventory_field
SET is_hidden_on_printout_ind = 'N'
WHERE insurance_plan_id = 3
AND is_hidden_on_printout_ind = 'Y';