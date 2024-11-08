
--Grant CRUD access to proxy role
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.verified_yield_contract TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.verified_yield_contract_commodity TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.verified_yield_amendment_code TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.verified_yield_amendment TO "app_cuws_rest_proxy";
GRANT SELECT, INSERT, UPDATE, DELETE ON cuws.product TO "app_cuws_rest_proxy";

--Grant read only access to all tables for the readonly role
GRANT SELECT ON ALL TABLES IN SCHEMA cuws TO "app_cuws_readonly";