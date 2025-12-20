# ---- Runtime image only (fast, small, secure) ----
FROM eclipse-temurin:17-jre-alpine

# Create non-root user (security, no behavior change)
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copy the built JAR
COPY target/*.jar app.jar

# Use non-root user
USER spring:spring

# Default Spring Boot port (documentation only)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]