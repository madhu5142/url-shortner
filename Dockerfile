#Docker container for a simple maven project url shortner.

FROM openjdk:17-jdk-slim AS build
WORKDIR /app
RUN apt-get update && apt-get install -y git
ARG GIT_REPO_URL
RUN git clone ${GIT_REPO_URL} .

RUN apt-get install -y maven

ARG BUILD_ENV
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app

COPY --from=build /app/target/mvp-*.jar app.jar

EXPOSE 8080

ARG INSTALL_REDIS=false
RUN if [ "$INSTALL_REDIS" = "true" ]; then \
    apt-get update && apt-get install -y redis-server && \
    redis-server --daemonize yes; \
    fi
EXPOSE 6379

ENV SPRING_PROFILES_ACTIVE=${BUILD_ENV}

ENTRYPOINT ["java", "-jar", "app.jar"]
