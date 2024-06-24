-- Role: "app_cuws"
-- DROP ROLE "app_cuws";

CREATE ROLE "app_cuws" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  CREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_ADMIN_PASSWORD}';
  
ALTER ROLE app_cuws SET search_path TO cuws;

ALTER USER app_cuws set TIMEZONE to 'America/New_York';

COMMENT ON ROLE "app_cuws" IS 'Cirras Underwriting System.';
