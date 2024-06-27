-- Role: "proxy_cuws_rest"
-- DROP ROLE "proxy_cuws_rest";

CREATE ROLE "proxy_cuws_rest" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_ADMIN_PASSWORD}';
  
ALTER ROLE proxy_cuws_rest SET search_path TO cuws;

ALTER USER proxy_cuws_rest set TIMEZONE to 'America/New_York';

COMMENT ON ROLE "proxy_cuws_rest" IS 'Proxy account for Cirras Underwriting System.';