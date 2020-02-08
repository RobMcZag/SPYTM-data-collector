# Data lake on Snowflake
This part of the application deals with loading stock data to a cloud data lake
hosted on Snowflake.

The basic principle is to load raw data as obtained by the collector into 
Snowflake for further use.

Data that can be loaded is both time series data and event data, such as the 
load of some data or any other thing happening in the app.
 
## Snowflake setup
To be able to use snowflake within the app in a safe way some setup is needed 
to provide for databases, schemas, roles and users and their privileges.

### Database and schema creation
The first thing to do is to create a database and schema to host the data lake, 
actually most probably one for test and one for production.

To create the DB and schema you need to execute the following with an user 
impersonating the role of `SYSADMIN` - the main user to create objects in SF.
```
--USE ROLE SYSADMIN;  - Imperson the sysadmin role, if your user has access to it.
CREATE DATABASE IF NOT EXISTS lake_test;
CREATE SCHEMA IF NOT EXISTS lake_test.raw; 
```

### User and Role setup
The second thing to do is to create the roles you need and the users to use them.  
To check the details look at [Snowflake - Overview of Access Control](https://docs.snowflake.net/manuals/user-guide/security-access-control-overview.html)
and [Snowflake - Access Control Privileges](https://docs.snowflake.net/manuals/user-guide/security-access-control-privileges.html).

To set up use and roles you need to execute the following with an user 
impersonating the role of `SECURITYADMIN` - the main user to manage user and roles in SF.
#### Create the roles 
Create all the roles you need to divide the privileges in a way you like.  
Below we are exemplifying a case where we have an owner of the DB and Schema
and a user who can create and use objects in an hypothetical `raw` schema.
```
CREATE ROLE IF NOT EXISTS LAKE_OWNER_ROLE;    -- if you want a DB specific owner
CREATE ROLE IF NOT EXISTS LAKE_USER_ROLE;     -- if one role with R/W permission is enough
```
#### Create the users 
Create the users you or your apps will use to log in.  
Assign the roles you just created as needed and if you want set their defaults.
```
CREATE USER <owner> PASSWORD = '...';
GRANT ROLE LAKE_OWNER_ROLE TO USER <owner>;

CREATE USER <user> PASSWORD = '...';
GRANT ROLE LAKE_USER_ROLE TO USER <user>;

ALTER USER <user> SET DEFAULT_ROLE = 'LAKE_USER_ROLE';
ALTER USER <user> SET DEFAULT_WAREHOUSE = <string>;
ALTER USER <user> SET DEFAULT_NAMESPACE = <string>;
```
Note that the users themselves can alter their defaults to avoid 
always needing `USE <db/schema>` to use unqualified names.

#### Assign the ownership
If you want a specific role to own the DB instead of the account `SYSADMIN` 
assign the ownership of the DB and of the schemas to the role you created for it:
```
GRANT OWNERSHIP ON DATABASE lake_test TO ROLE LAKE_OWNER_ROLE;
GRANT OWNERSHIP ON SCHEMA lake_test.raw TO ROLE LAKE_OWNER_ROLE;
```
#### Define the privileges for the other roles
Assign the privileges to the other roles so that they can do their job.  
If you have created an owner role, the below could also be done by that role.
```
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

```

### Setup files 
In the folder "setup"   