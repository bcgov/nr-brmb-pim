-- Export fields associated with Berries Policies
select STRING_AGG (p.policy_number, ', ' ORDER BY policy_number) as "Policy List", 
       f.field_id as "Field ID", 
       f.field_label as "Field Name",
       f.active_from_crop_year as "Field Active From",
       f.active_to_crop_year as "Field Active To",
       l.legal_land_id as "Legal Land ID",
       case when l.legal_land_id = afd.legal_land_id then 'Y' else 'N' end as "Is Primary",
       l.other_description as "Other Description",
       l.legal_description as "Legal Description",
       l.primary_property_identifier as "Property Identifier",
       l.primary_land_identifier_type_code as "Property Identifier Type",
       l.active_from_crop_year as "Legal Active From",
       l.active_to_crop_year as "Legal Active To",
       l.total_acres as "Legal Acres",
       ra.risk_area_name as "Risk Area"
from field f
join annual_field_detail afd on f.field_id = afd.field_id 
join contracted_field_detail cfd on afd.annual_field_detail_id = cfd.annual_field_detail_id 
join grower_contract_year gcy on cfd.grower_contract_year_id = gcy.grower_contract_year_id 
join policy p on gcy.contract_id = p.contract_id 
             and gcy.crop_year = p.crop_year 
left join legal_land_field_xref lx on f.field_id = lx.field_id 
left join legal_land l on l.legal_land_id = lx.legal_land_id 
left join legal_land_risk_area_xref rax on rax.legal_land_id = l.legal_land_id 
left join risk_area ra on ra.risk_area_id = rax.risk_area_id 
                      and ra.insurance_plan_id = 3 
where gcy.insurance_plan_id = 3 
group by f.field_id, 
         f.field_label, 
         f.active_from_crop_year, 
         f.active_to_crop_year, 
         l.legal_land_id, 
         l.other_description, 
         l.legal_description, 
         l.primary_property_identifier, 
         l.primary_land_identifier_type_code, 
         l.active_from_crop_year, 
         l.active_to_crop_year, 
         l.total_acres, 
         ra.risk_area_name, 
         afd.legal_land_id 
order by f.field_id, 
         l.other_description, 
         l.legal_description, 
         l.primary_property_identifier; 
