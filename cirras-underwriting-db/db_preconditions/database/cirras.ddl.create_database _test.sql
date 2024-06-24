-- Database: cirrastst

-- DROP DATABASE "cirrastst";

CREATE DATABASE "cirrastst"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "cirrastst"
    IS 'cirrastst database containing schemas used by cirras applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "cirrastst" TO PUBLIC;

GRANT ALL ON DATABASE "cirrastst" TO postgres;