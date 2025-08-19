-- Role: "PROXY_DAP_PIUW"
-- DROP ROLE "PROXY_DAP_PIUW";

CREATE ROLE "PROXY_DAP_PIUW" WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  PASSWORD '${POSTGRES_PROXY_DAP_PASSWORD}';
  
ALTER ROLE "PROXY_DAP_PIUW" SET search_path TO cuws;

COMMENT ON ROLE "PROXY_DAP_PIUW" IS 'Proxy account for DAP read only access to Cirras Underwriting System';

--GRANT "app_cuws_readonly" TO "PROXY_DAP_PIUW"; --Grant has to be applied in the terminal in the master POD