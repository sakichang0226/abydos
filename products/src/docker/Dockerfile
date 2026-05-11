FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/products.jar products.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","products.jar","--spring.profiles.active=dev"]
