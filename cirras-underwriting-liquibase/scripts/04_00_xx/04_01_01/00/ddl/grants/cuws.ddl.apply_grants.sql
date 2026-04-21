
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_field_commodity_berries TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_field_variety_berries TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_contract_commodity_berries TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.commodity_maturity_scale TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.declared_yield_contract_commodity_berries_ob TO "app_cuws_rest_proxy";

--Grant access to sequences to proxy
GRANT USAGE ON ALL SEQUENCES IN SCHEMA cuws TO "app_cuws_rest_proxy";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA cuws TO "app_cuws_readonly";
