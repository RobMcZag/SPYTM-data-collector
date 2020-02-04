# PRODUCTION -SQL based- Flyway migrations

**This folder is for _SQL Flyway database migrations_ to be applied in PRODUCTION**.

The JAVA based Flyway migrations for production are in `java/db/migration`.

Quick link to [test migrations README](../../../../../src/test/resources/db/migration/README.md)
that includes more info on the test and release process for DB migrations.

### Why SQL DB migrations
Typically SQL based migrations are used to perform structural operations on the DB (DDL),
like adding tables, indexes or columns.

They are also used to perform basic data migration tasks,
like moving data from one table to another, entering defaults or recalculating values.

### Why JAVA DB migrations
Data tasks tha require specific checks (even external to the DB) or data manipulation 
that are not simple to perform in SQL can be done with Java migrations.  
