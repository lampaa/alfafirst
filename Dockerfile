FROM openjdk:11.0.5-jdk
COPY src/main/resources/apidevelopers.jks /var/www/apidevelopers.jks
COPY target/first-0.0.1.jar /var/www/first-0.0.1.jar
WORKDIR /var/www

EXPOSE 8080