FROM amazoncorretto:21-alpine
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-XX:+ExitOnOutOfMemoryError","-Dcom.sun.management.jmxremote","-Dcom.sun.management.jmxremote.port=5050","-Dcom.sun.management.jmxremote.ssl=false","-Dcom.sun.management.jmxremote.authenticate=false","-jar","/app.jar"]