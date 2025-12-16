\qecho 'Insert fields and legal_lands'

\qecho 'Insert new fields:'

-- Generate new field ids
with fld_new as (
        select v.contract_number,
               v.crop_year,	
               v.field_bog_name, 
               v.field_location, 
               v.primary_property_identifier,
               v.primary_land_identifier_type_code,
               nextval('fld_seq') as new_field_id
        from berries_2026_staging v
        where v.field_id is null
        group by v.contract_number,
                 v.crop_year,
                 v.field_bog_name, 
                 v.field_location, 
                 v.primary_property_identifier,
                 v.primary_land_identifier_type_code
)
update berries_2026_staging t
set field_id = (select fld_new.new_field_id
                from fld_new
                where fld_new.contract_number = t.contract_number
                  and fld_new.crop_year = t.crop_year
                  and fld_new.field_bog_name = t.field_bog_name
                  and coalesce(fld_new.field_location, '') = coalesce(t.field_location, '')
                  and fld_new.primary_property_identifier = t.primary_property_identifier
                  and fld_new.primary_land_identifier_type_code = t.primary_land_identifier_type_code
               )
where t.field_id is null;


insert into field (
        field_id, 
        field_label,
        location,	
        active_from_crop_year, 
        active_to_crop_year, 
        create_user, 
        create_date, 
        update_user, 
        update_date
) select t.field_id,
         t.field_bog_name,
         t.field_location,
         2026,
         null,
         'CUWS_04_00_00',
         now(),
         'CUWS_04_00_00',
         now()
  from (select bs.field_id,
               bs.field_bog_name,
               bs.field_location
        from berries_2026_staging bs
        where bs.field_id is not null
        group by bs.field_id,
                 bs.field_bog_name,
                 bs.field_location
       ) t;

\qecho 'Try to match to existing legal land:'
update berries_2026_staging bs
set legal_land_id = existing_legal_land.existing_ll_id,
    do_insert_legal_land_ind = 'N'
from (select ll.primary_property_identifier,
             ll.primary_land_identifier_type_code,
             min(ll.legal_land_id) as existing_ll_id    -- In case of multiple matches, pick one.
      from legal_land ll
      where ll.active_from_crop_year <= 2026
        and (ll.active_to_crop_year is null or ll.active_to_crop_year > 2026)
      group by ll.primary_property_identifier,
               ll.primary_land_identifier_type_code
     ) existing_legal_land
where bs.primary_property_identifier = existing_legal_land.primary_property_identifier
  and bs.primary_land_identifier_type_code = existing_legal_land.primary_land_identifier_type_code
  and bs.legal_land_id is null;

\qecho 'Insert new legals:'
-- Generate new legal land ids
with ll_new as (
	select v.primary_property_identifier,
               v.primary_land_identifier_type_code,
               nextval('ll_seq') as new_legal_land_id
	from berries_2026_staging v
	where v.legal_land_id is null
	group by v.primary_property_identifier,
                 v.primary_land_identifier_type_code
)
update berries_2026_staging t
set (legal_land_id, do_insert_legal_land_ind) = (select ll_new.new_legal_land_id, 'Y'
                                                 from ll_new
                                                 where ll_new.primary_property_identifier = t.primary_property_identifier
                                                   and ll_new.primary_land_identifier_type_code = t.primary_land_identifier_type_code
                                                )
where t.legal_land_id is null;

-- Insert legal lands
insert into legal_land(
        legal_land_id, 
        primary_reference_type_code, 
        legal_description, 
        legal_short_description, 
        other_description, 
        active_from_crop_year, 
        active_to_crop_year, 
        create_user, 
        create_date, 
        update_user, 
        update_date, 
        primary_property_identifier, 
        total_acres, 
        primary_land_identifier_type_code
) select t.legal_land_id,
         'IDENTIFIER',
         null,
         null,
         null,
         2026,
         null,
         'CUWS_04_00_00',
         now(),
         'CUWS_04_00_00',
         now(),
         t.primary_property_identifier,
         null,
         t.primary_land_identifier_type_code
  from (select bs.legal_land_id,
               bs.primary_property_identifier,
               bs.primary_land_identifier_type_code
        from berries_2026_staging bs
        where bs.legal_land_id is not null
          and bs.do_insert_legal_land_ind = 'Y'
        group by bs.legal_land_id, 
                 bs.primary_property_identifier,
                 bs.primary_land_identifier_type_code
       ) t;

\qecho 'Insert legal_land_field_xref:'

insert into legal_land_field_xref(
        legal_land_id, 
        field_id, 
        create_user, 
        create_date, 
        update_user, 
        update_date
) select t.legal_land_id,
         t.field_id,
         'CUWS_04_00_00',
         now(),
         'CUWS_04_00_00',
         now()
  from (select bs.field_id,
               bs.legal_land_id
        from berries_2026_staging bs
        where bs.field_id is not null
          and bs.legal_land_id is not null
        group by bs.field_id,
                 bs.legal_land_id
       ) t;

\qecho 'Insert annual_field_detail:'

insert into annual_field_detail (
        annual_field_detail_id, 
        legal_land_id, 
        field_id, 
        crop_year, 
        create_user, 
        create_date, 
        update_user, 
        update_date
) select nextval('afd_seq'),
         t.legal_land_id,
         t.field_id,
         t.crop_year,
         'CUWS_04_00_00',
         now(),
         'CUWS_04_00_00',
         now()
  from (select bs.field_id,
               bs.legal_land_id,
               bs.crop_year
        from berries_2026_staging bs
        where bs.field_id is not null
        group by bs.field_id,
                 bs.legal_land_id,
                 bs.crop_year
       ) t;

\qecho 'Insert contracted_field_detail:'

insert into contracted_field_detail (
        contracted_field_detail_id, 
        annual_field_detail_id, 
        grower_contract_year_id, 
        display_order,
        is_leased_ind,	
        create_user, 
        create_date, 
        update_user, 
        update_date
) select nextval('cfd_seq'),
         t.annual_field_detail_id,
         t.grower_contract_year_id,
         t.min_field_order,
         case when t.ownership = 'LEASED' then 'Y' else 'N' end,
         'CUWS_04_00_00',
         now(),
         'CUWS_04_00_00',
         now()
  from (select bs.grower_contract_year_id,
               min(bs.input_line_number) as min_field_order,         -- In case of multiple plantings, pick the lowest one.
               min(bs.ownership),
               afd.annual_field_detail_id
        from berries_2026_staging bs
        join annual_field_detail afd on afd.field_id = bs.field_id and afd.crop_year = bs.crop_year
        group by bs.grower_contract_year_id,
                 afd.annual_field_detail_id
       ) t;

\qecho 'Re-calculate field display order to eliminate gaps:'

with field_orders as (
        select cfd.contracted_field_detail_id, row_number() OVER (PARTITION BY cfd.grower_contract_year_id
                                                                  ORDER BY cfd.display_order,
                                                                           cfd.contracted_field_detail_id
                                                                 ) as rn
        from contracted_field_detail cfd
        where cfd.grower_contract_year_id in (select bs.grower_contract_year_id
                                              from berries_2026_staging bs
                                             )
)
update contracted_field_detail t
set display_order = field_orders.rn,
    update_user = 'CUWS_04_00_00',
    update_date = now()
from field_orders
where field_orders.contracted_field_detail_id = t.contracted_field_detail_id;


\qecho 'Create comments for secondary pids'
insert into underwriting_comment(
  underwriting_comment_guid,
  underwriting_comment_type_code,
  underwriting_comment,
  create_user,
  create_date,
  update_user,
  update_date,
  annual_field_detail_id,
  grower_contract_year_id,
  declared_yield_contract_guid,
  verified_yield_summary_guid
) select replace(cast(gen_random_uuid() as text), '-', ''),
         'INV',
         'Other Property Identifiers associated with this field: ' || bs.secondary_property_identifiers,
         'CUWS_04_00_00',
         now(),
         'CUWS_04_00_00',
         now(),
         afd.annual_field_detail_id,
         null,
         null,
         null
  from berries_2026_staging bs
  join annual_field_detail afd on afd.field_id = bs.field_id and afd.crop_year = bs.crop_year
  where secondary_property_identifiers is not null
  group by bs.field_id,
           bs.crop_year,
           afd.annual_field_detail_id,
           bs.secondary_property_identifiers;

