create or replace procedure begin_release(
   p_application_version   in application_release.application_version%type,
   p_revision_number       in application_release.revision_number%type,
   p_release_description   in application_release.release_description%type,
   p_create_user            in application_release.create_user%type
)
language plpgsql    
as $$
begin

    insert into application_release (
        application_version, 
        revision_number, 
        deployment_date, 
        release_description, 
        deployment_complete_ind, 
        create_user, 
        create_date, 
        update_user, 
        update_date
    ) values (
        p_application_version,
        p_revision_number, 
        now(), 
        p_release_description, 
        'N', 
        p_create_user, 
        now(), 
        p_create_user, 
        now()
   )
   ON CONFLICT DO NOTHING
   ;

end;$$