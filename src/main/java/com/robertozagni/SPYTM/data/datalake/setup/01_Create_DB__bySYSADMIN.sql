-- IMPORTANT !
-- You need to impersonate the sysadmin role. Your user need to have access to it.
USE ROLE SYSADMIN;

-- Create the TEST data lake and raw schema
CREATE DATABASE IF NOT EXISTS lake_test;
CREATE SCHEMA IF NOT EXISTS lake_test.raw;
