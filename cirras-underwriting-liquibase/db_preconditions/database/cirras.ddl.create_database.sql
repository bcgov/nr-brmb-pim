-- Database: pituw${ENV}

-- DROP DATABASE "pituw${ENV}";

CREATE DATABASE "pituw${ENV}"
    WITH 
    OWNER = postgres
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "pituw${ENV}"
    IS 'pituw${ENV} database containing schemas used by production insurance applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "pituw${ENV}" TO PUBLIC;

GRANT ALL ON DATABASE "pituw${ENV}" TO postgres;