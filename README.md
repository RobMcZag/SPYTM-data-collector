[![Build Status](https://travis-ci.com/RobMcZag/SPYTM-data-collector.svg?branch=master)](https://travis-ci.com/RobMcZag/SPYTM-data-collector)

# SPYTM-data-collector

This is just a small Spring Boot application I wrote to brush off some rust from my hands-on 
coding skills and to somehow organise the collection of data for my investment activities.  
For the courious... SPYTM stands for SPY Trading Model.

The basic idea of this application is to collect stock data from different available 
data sources and store it for further use.

The initial intended use of the downloaded data is to train a model, written in Python, 
to predict future SPY prices from other price time-series, using Linear Regression.
One remarkably good relationships exists between the daily closing / half-day price 
of Asian / European markets and the US markets as represented by SPY.  
The basic idea is to exploit the fact that the Asian / European markets provide their
closing / half-day time early enough to bet on the daily outcome of the US markets.     

The python regression model will be available in another repo. ATM it is still unpublished. 

## Main featues
* The data is collected through API calls, receiving JSON encoded data.
* The data can be downloaded incrementally, adding what is new or changed.
* The data model of the application is independent of each provider's format.
  * For each provider it is nice and easy to use a specific data model.   
  This supports easy download and deserialisation of data as sent over the wire.
  * Each provider's downloader keeps its own model private.   
  It delivers data to the rest of the app in the app's canonical data model.

### Future ideas
* data pipeline to deliver downloaded data to a cloud data lake
* export data to CSV in YahooFinance format
* minimal web interface to explore the downloaded content 
* rest API to access data from the local DB
* reporting and data sharing on top of the cloud data lake
 

## Unsorted techies things :)
* Build is done with Gradle and checked upon push and merge with Travis CI.  
* Data is stored in a configurable DB - in memory H2 by default;  
Postgress is already configured, just enable the `test_pg` profile.  
* The Travis build script `.travis.yml` includes commands to create the PG Database and user.    
You can use those commands to create your local DB and app user.
* Database evolution is managed through Flyway migrations (SQL or Java)  
Get more in the [Flyway migrations README](src/main/resources/db/migration/README.md)
* Tests are nicely organised :)
  * Unit tests are separated from Integration tests and each set can be run independently.  
  The default if you just run the `test` task is to only run Unit tests.
  * both Unit and Integration tests are run upon build and in CI
  * a test source-set library holds static test data and helpers to make available 
  that data to all the tests.  


# Running the project
If you want to use a real DB instead of the embedded H2, 
just have the server running and the connection configured in spring boot.
For more see below [Setting up a real DB](#DBsetup).

* to test  - run unit OR integration tests OR both  
`./gradlew test` | `./gradlew integrationTest` | `./gradlew check`   

* to build `./gradlew build`
* to run the project `./gradlew bootRun`
  * to access H2 console (while app is running) go to 
  `http://localhost:8080/h2-console`


# <A id="DBsetup"></A> Setting up a real DB 
Below are the steps to set-up a Postgress DB instead of using the default in-memory H2.

## Postgres Setup
* Install Postgres where you want to run it and start it with something like  
`pg_ctl -D /usr/local/var/postgres start`

* login to Postgres from your command line `sql postgres`  
This usually is enough for how PG comes preconfigured
  
* Create user to manage the DB  
`CREATE USER <user name>> PASSWORD '<pw>>';`

* Creat the DB  
`CREATE DATABASE <db name>;`

* Assign ownership of DB to user  
`ALTER DATABASE <db name>> OWNER TO <user name>>;`

* Edit the PG access file `pg_hba.conf` to stop trusting everyone! :)  
The file should be in `/usr/local/var/postgres/`

## Spring Setup
After you have setup your DB, created a DB and an user for the app, 
you just have to tell those things to your Spring app.
  
* Add PG drivers in the dependencies config in `build.gradle` (as I use Gradle)  
`runtimeOnly 'org.postgresql:postgresql'` will do the trick
* Spring config file:
    * Create Spring profiles for `test` and `prod` with `spring.profiles` property;
    * Select which one to activate with `spring.profiles.active` property;
    * Configure Spring datasource and JPA properties for each profile, like
```
spring:
   datasource:
     url: jdbc:postgresql://localhost:5432/spytm_test_db
     username: spytm_test_user
     password: 1234-test-PW
```
* Add `@EnableTransactionManagement` on the main Application class to use transactions.
