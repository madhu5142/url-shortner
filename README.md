A Readme for running Spring application.<br><br>
## Pre-requisites

Java 17 and maven

## How to run ?

Run script.bat to launch application.<br>

### Test

Access http://localhost:8080/swagger-ui/index.html to visit swagger page and access all api

Otherwise use postman with http://localhost:8080/ to test api's.

###Database

The database used here is H2 internal database. We can switch to MySQL or PostgreSQL through properties file.

###Caching
Install redis server to use caching. Define redis port in properties file.