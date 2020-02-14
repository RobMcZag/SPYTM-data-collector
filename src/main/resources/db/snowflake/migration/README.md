# SNOWFLAKE Flyway migrations

**This folder is for _Flyway database migrations_ to be applied in SNOWFLAKE 
data lake. **  

#### URL format	
`jdbc:snowflake://account.snowflakecomputing.com/?db=database&warehouse=warehouse&role=role (optionally &schema=schema to specify current schema)`

See [Snowflake support on Flyway](https://flywaydb.org/documentation/database/snowflake)
for more info.

NOTE - this application uses two different set of migrations: 
one for the local DB holding the downloaded data and one for the 
cloud data lake (Snowflake). This folder is for the latter.  
 