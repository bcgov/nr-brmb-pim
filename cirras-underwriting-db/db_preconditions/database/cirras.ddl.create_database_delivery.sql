-- Database: cirrasdlv

-- DROP DATABASE "cirrasdlv";

CREATE DATABASE "cirrasdlv"
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

COMMENT ON DATABASE "cirrasdlv"
    IS 'cirrasdlv database containing schemas used by cirras applications and services.';

GRANT TEMPORARY, CONNECT ON DATABASE "cirrasdlv" TO PUBLIC;

GRANT ALL ON DATABASE "cirrasdlv" TO postgres;