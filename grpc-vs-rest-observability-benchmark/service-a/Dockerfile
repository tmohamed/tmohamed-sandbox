FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar /app/opentelemetry-javaagent.jar
COPY target/service-a-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081 9091
ENTRYPOINT ["java", "-javaagent:/app/opentelemetry-javaagent.jar", "-jar", "app.jar"]