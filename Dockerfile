##
# BUILD
##
FROM maven:3.6.3-jdk-17.0.5 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
##
# RUN
##
FROM openjdk:17.0.5
COPY --from=build /home/app/target/demo-0.0.1-SNAPSHOT.jar /usr/local/lib/demo.jar
EXPOSE 8080
ENTRYPOINT [ "java","jar","/usr/local/lib/demo.jar" ]