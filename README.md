A Readme for running Spring application.<br><br>
## Pre-requisites

Java 17 and maven
OR
Docker

## How to run ?

Run script.bat to launch application.<br>

### Test

Access http://localhost:8080/swagger-ui/index.html to visit swagger page and access all api

Otherwise use postman with http://localhost:8080/ to test api's.

###Database

The database used here is H2 internal database. We can switch to MySQL or PostgreSQL through properties file.

###Caching
Install redis server to use caching. Define redis port in properties file.

##Docker File
Build the application with docker with customization to install Redis server and select run environment.

Build docker image
```
docker build --build-arg GIT_REPO_URL=https://github.com/madhu5142/url-shortner.git \
             --build-arg BUILD_ENV=local \
             --build-arg INSTALL_REDIS=true \
             -t urlshortener-app .
```

Run docker image
```
docker run -p 8080:8080 -p 6379:6379 urlshortener-app
```