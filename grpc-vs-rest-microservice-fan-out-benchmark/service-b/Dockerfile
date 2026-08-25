FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/service-b-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082 9092
ENTRYPOINT ["java", "-jar", "app.jar"]