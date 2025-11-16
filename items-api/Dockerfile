FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/items-api.jar items-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","items-api.jar","--spring.profiles.active=dev"]
