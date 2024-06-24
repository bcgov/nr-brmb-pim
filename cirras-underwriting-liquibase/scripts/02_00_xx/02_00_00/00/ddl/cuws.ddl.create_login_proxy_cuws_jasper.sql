-- Role: "proxy_cuws_jasper"
-- DROP ROLE "proxy_cuws_jasper";

CREATE ROLE "proxy_cuws_jasper" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD 'password';
  
ALTER ROLE proxy_cuws_jasper SET search_path TO cuws;

COMMENT ON ROLE "proxy_cuws_jasper" IS 'Proxy account for Cirras Underwriting System Jasper Reports';