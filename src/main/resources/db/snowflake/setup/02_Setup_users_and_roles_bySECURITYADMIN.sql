-- IMPORTANT !
-- You need to impersonate the securityadmin role. Your user need to have access to it.
USE ROLE SECURITYADMIN;

-- OWNER ROLE SETUP
--Create "owner" role and user, if you want a DB specific owner instead of the generic SYSADMIN
CREATE ROLE IF NOT EXISTS LAKE_OWNER_ROLE;
CREATE USER l_owner PASSWORD = 'bla...bla';
GRANT ROLE LAKE_OWNER_ROLE TO USER l_owner;

ALTER USER l_owner SET DEFAULT_ROLE = LAKE_OWNER_ROLE;
-- ALTER USER <user> SET DEFAULT_WAREHOUSE = <string>;
-- ALTER USER <user> SET DEFAULT_NAMESPACE = <string>;

-- Grant ownership of the DB and schema to the owner role
GRANT OWNERSHIP ON DATABASE lake_test TO ROLE LAKE_OWNER_ROLE;
GRANT OWNERSHIP ON SCHEMA lake_test.raw TO ROLE LAKE_OWNER_ROLE;


-- USER ROLE SETUP
--Create "user" role and user, if one role with Create & R/W permission is enough
CREATE ROLE IF NOT EXISTS LAKE_USER_ROLE;
CREATE USER l_user PASSWORD = 'x...x';
GRANT ROLE LAKE_USER_ROLE TO USER l_user;

ALTER USER l_user SET DEFAULT_ROLE = 'LAKE_USER_ROLE';
-- ALTER USER <user> SET DEFAULT_WAREHOUSE = <string>;
-- ALTER USER <user> SET DEFAULT_NAMESPACE = <string>;

-- Grant desired privileges to <user> role
GRANT USAGE ON DATABASE lake_test TO ROLE LAKE_USER_ROLE;

GRANT
    USAGE,
    MONITOR,
    CREATE TABLE,
    CREATE VIEW,
    CREATE STAGE
    ON SCHEMA lake_test.raw TO ROLE LAKE_USER_ROLE;

GRANT ALL ON FUTURE TABLES IN SCHEMA lake_test.raw TO ROLE LAKE_USER_ROLE;

GRANT ALL ON FUTURE VIEWS IN SCHEMA lake_test.raw TO ROLE LAKE_USER_ROLE;

GRANT ALL ON FUTURE STAGES IN SCHEMA lake_test.raw TO ROLE LAKE_USER_ROLE;
