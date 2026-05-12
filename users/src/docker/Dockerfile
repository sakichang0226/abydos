FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/users.jar users.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","users.jar","--spring.profiles.active=dev"]
