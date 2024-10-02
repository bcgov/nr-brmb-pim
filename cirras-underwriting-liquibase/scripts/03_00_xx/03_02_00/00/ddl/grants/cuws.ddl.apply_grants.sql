GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_field_forage TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_contract_cmdty_forage TO "app_cuws_rest_proxy";


--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA cuws TO "app_cuws_readonly";
