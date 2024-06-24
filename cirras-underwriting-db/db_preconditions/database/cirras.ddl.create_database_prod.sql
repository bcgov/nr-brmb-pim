-- Database: cirrasprd

-- DROP DATABASE "cirrasprd";

CREATE DATABASE "cirrasprd"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "cirrasprd"
    IS 'cirrasprd database containing schemas used by cirras applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "cirrasprd" TO PUBLIC;

GRANT ALL ON DATABASE "cirrasprd" TO postgres;