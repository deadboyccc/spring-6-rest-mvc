FROM eclipse-temurin:17-jre-alpine

ENV JAVA_OPTS="-XX:+UseContainerSupport"

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY target/*.jar /app/app.jar

RUN chown -R spring:spring /app

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]