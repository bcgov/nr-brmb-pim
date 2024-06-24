create or replace procedure end_release(
   p_application_version   in application_release.application_version%type,
   p_revision_number       in application_release.revision_number%type
)
language plpgsql    
as $$
begin

    update application_release
    set deployment_complete_ind = 'Y',	
    update_date = now()
    where application_version = p_application_version
    and revision_number = p_revision_number
    and deployment_date = (select max(deployment_date) 
                           from application_release 
                           where application_version = p_application_version 
                           and revision_number = p_revision_number);

end;$$