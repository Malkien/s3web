##
# BUILD
##
FROM maven:3.6.3-openjdk-17 AS build
COPY . .
RUN mvn clean package
##
# RUN
##
FROM openjdk:17
COPY --from=build /home/target/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java","jar","app.jar" ]