FROM --platform=linux/amd64 adoptopenjdk/openjdk11:alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]