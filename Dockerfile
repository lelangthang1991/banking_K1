FROM maven:latest AS BUILD

EXPOSE 8080
EXPOSE 3306

WORKDIR /usr/src/app
COPY . /usr/src/app

# Compile and package the application to an executable JAR
RUN mvn package -DskipTests

RUN echo "running in root"
RUN ls
RUN echo "running in /usr/src/app"
RUN ls /usr/src/app
RUN echo "running in /usr/src/app/target"
RUN ls /usr/src/app/target


# For Java 11,
FROM adoptopenjdk/openjdk11:alpine-jre AS APP

ARG JAR_FILE=banking-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app

# Copy the banking-0.0.1-SNAPSHOT.jar from the maven stage to the /opt/app directory of the current stage.
COPY --from=BUILD /usr/src/app/target/${JAR_FILE} /opt/app/

ENTRYPOINT ["java","-jar","banking-0.0.1-SNAPSHOT.jar"]
