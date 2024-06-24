-- Database: cirrastst

-- DROP DATABASE "cirrastst";

CREATE DATABASE "cirrasdev"
    WITH 
    OWNER = postgres
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "cirrasdev"
    IS 'cirrasdev database containing schemas used by cirras applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "cirrasdev" TO PUBLIC;

GRANT ALL ON DATABASE "cirrasdev" TO postgres;