-- SCHEMA: cuws

-- DROP SCHEMA "cuws" ;

CREATE SCHEMA "cuws"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "cuws" TO "app_cuws";

GRANT USAGE ON SCHEMA "cuws" TO "app_cuws_rest_proxy";

GRANT ALL ON SCHEMA "cuws" TO postgres;