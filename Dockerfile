FROM amazoncorretto:21-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-jar","/app.jar"]