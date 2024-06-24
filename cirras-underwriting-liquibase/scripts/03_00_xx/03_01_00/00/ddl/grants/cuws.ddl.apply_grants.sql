GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.underwriting_year TO "app_cuws_rest_proxy";


--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA cuws TO "app_cuws_readonly";
