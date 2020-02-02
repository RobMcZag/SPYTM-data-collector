[![Build Status](https://travis-ci.com/RobMcZag/SPYTM-data-collector.svg?branch=master)](https://travis-ci.com/RobMcZag/SPYTM-data-collector)

# SPYTM-data-collector
This small Spring Boot application will collect stock data from different available 
data sources and store it (in DB and/or in CSV files) for further use.

The data is collected or through API calls, receiving JSON encoded data,
or by scraping available sources, possibly by using page-internal data 
storage in JSON or XML.

The use of data is intended to train a model, written in Python, to predict future
securities prices from the past ones, using Linear Regression.
One remarkably good relationships exists between the daily closing / half-day price 
of Asian / European markets and the US markets. 
The basic idea is to exploit the fact that the Asian / European markets provide their
closing / half-day time early enough to bet on the daily outcome of the US markets.     

# Setting up the DB

## Postgres Setup
* Create user to manage the DB  
`CREATE USER <user name>> PASSWORD '<pw>>';`

* Creat the DB  
`CREATE DATABASE <db name>;`

* Assign ownership of DB to user  
`ALTER DATABASE <db name>> OWNER TO <user name>>;`

* Edit the PG access file `pg_hba.conf` to stop trusting everyone! :)  
The file should be in `/usr/local/var/postgres/`

## Spring Setup
* Add PG drivers in the dependencies config in `build.gradle` (as I use Gradle)
* Spring config file:
    * Create Spring profiles for `test` and `prod` with `spring.profiles` property;
    * Select which one to activate with `spring.profiles.active` property;
    * Configure Spring datasource and JPA properties.
* Add `@EnableTransactionManagement` to the main Application class to use transactions.

# Running the project
* to build `./gradlew build`
* to test `./gradlew test`
* to run the project `./gradlew bootRun`  

Note: use `gradlew.bat` instead of `./gradlew` in a Windows OS 