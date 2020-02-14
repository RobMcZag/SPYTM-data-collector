-- IMPORTANT !
-- You need to impersonate the accoundadmin role. Your user need to have access to it.
USE ROLE ACCOUNTADMIN;

-- Set some basic properties at account level
ALTER ACCOUNT SET TIMEZONE = 'UTC';
ALTER ACCOUNT SET TIMESTAMP_TYPE_MAPPING = 'TIMESTAMP_NTZ';         -- NO Timezone
ALTER ACCOUNT SET CLIENT_TIMESTAMP_TYPE_MAPPING = 'TIMESTAMP_NTZ';  -- default would be ..._Ltz (Local TZ)

