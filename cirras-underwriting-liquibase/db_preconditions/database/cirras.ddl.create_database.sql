-- Database: cirras${ENV}

-- DROP DATABASE "cirras${ENV}";

CREATE DATABASE "cirras${ENV}"
    WITH 
    OWNER = postgres
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "cirras${ENV}"
    IS 'cirras${ENV} database containing schemas used by cirras applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "cirras${ENV}" TO PUBLIC;

GRANT ALL ON DATABASE "cirras${ENV}" TO postgres;