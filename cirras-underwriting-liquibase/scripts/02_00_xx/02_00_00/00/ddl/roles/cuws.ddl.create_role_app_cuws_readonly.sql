-- Role: "app_cuws_readonly"
-- DROP ROLE "app_cuws_readonly";

CREATE ROLE "app_cuws_readonly";
GRANT "app_cuws_readonly" TO "proxy_cuws_jasper";

GRANT USAGE ON SCHEMA "cuws" TO "app_cuws_readonly";
