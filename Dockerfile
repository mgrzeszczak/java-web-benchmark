FROM amazoncorretto:21-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
COPY entrypoint.sh /entrypoint.sh
ENTRYPOINT ["./entrypoint.sh", "/app.jar"]