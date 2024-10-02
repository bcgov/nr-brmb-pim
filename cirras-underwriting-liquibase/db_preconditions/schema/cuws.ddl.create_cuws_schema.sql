-- SCHEMA: cuws

-- DROP SCHEMA "cuws" ;

CREATE SCHEMA "cuws"
    AUTHORIZATION postgres;

GRANT ALL ON SCHEMA "cuws" TO "app_cuws";

GRANT ALL ON SCHEMA "cuws" TO postgres;

ALTER SCHEMA "cuws" OWNER TO "app_cuws";