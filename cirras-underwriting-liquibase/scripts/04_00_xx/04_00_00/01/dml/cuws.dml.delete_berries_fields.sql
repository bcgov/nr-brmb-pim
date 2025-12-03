-- Delete any existing Berries Inventory data, and any existing fields
-- associated with only Berries policies.

-- Delete inventory_berries
delete from inventory_berries;

-- Delete inventory_field
delete from inventory_field where insurance_plan_id = 3;

-- Delete inventory_contract_commodity_berries
delete from inventory_contract_commodity_berries;

-- Delete inventory_contract
delete from inventory_contract ic
where exists (select * from grower_contract_year gcy
              where gcy.contract_id = ic.contract_id
              and gcy.crop_year = ic.crop_year 
              and gcy.insurance_plan_id = 3);

-- insert the annual field data that is going to be deleted in a temp table
insert into berries_delete_annual_data_staging (field_id, annual_field_detail_id, contracted_field_detail_id )
select field_id, cfd.annual_field_detail_id, cfd.contracted_field_detail_id  
from contracted_field_detail cfd
join annual_field_detail afd on afd.annual_field_detail_id = cfd.annual_field_detail_id
join grower_contract_year gcy on cfd.grower_contract_year_id = gcy.grower_contract_year_id
where gcy.insurance_plan_id = 3; 

-- Delete contracted_field_detail
delete from contracted_field_detail cfd
where cfd.contracted_field_detail_id in (select bdads.contracted_field_detail_id from berries_delete_annual_data_staging bdads);

-- Delete underwriting_comment
delete from underwriting_comment uc
where uc.annual_field_detail_id in (select bdads.annual_field_detail_id from berries_delete_annual_data_staging bdads)
  and uc.annual_field_detail_id not in (select cfd.annual_field_detail_id from contracted_field_detail cfd)
  and uc.annual_field_detail_id not in (select afd.annual_field_detail_id 
                                        from annual_field_detail afd 
                                        join inventory_field ifd on ifd.field_id = afd.field_id
                                                                and ifd.crop_year = afd.crop_year);

-- Delete annual_field_detail record
delete from annual_field_detail afd
where afd.annual_field_detail_id in (select bdads.annual_field_detail_id from berries_delete_annual_data_staging bdads)
  and afd.annual_field_detail_id not in (select cfd.annual_field_detail_id from contracted_field_detail cfd)
  and afd.annual_field_detail_id not in (select afd2.annual_field_detail_id
                                         from annual_field_detail afd2
                                         join inventory_field ifd on ifd.field_id = afd2.field_id
                                                                 and ifd.crop_year = afd2.crop_year);

-- Delete legal_land_field_xref
delete from legal_land_field_xref llfx
where llfx.field_id in (select bdads.field_id from berries_delete_annual_data_staging bdads)
  and llfx.field_id not in (select afd.field_id from annual_field_detail afd);

-- Delete field
delete from field f
where f.field_id in (select bdads.field_id from berries_delete_annual_data_staging bdads)
  and f.field_id not in (select afd.field_id from annual_field_detail afd);

