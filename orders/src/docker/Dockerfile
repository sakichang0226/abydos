FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/orders.jar orders.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","orders.jar","--spring.profiles.active=dev"]
