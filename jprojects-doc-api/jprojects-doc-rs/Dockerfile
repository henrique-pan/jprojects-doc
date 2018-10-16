FROM openjdk:8-jdk-alpine
VOLUME /temp/volume
ENV CONFIG-DIRECTORY /temp/volume/config
# docker run -p 8080:8080 --mount type=bind,source=/temp/volume,target=/temp/volume -t jprojects-doc-r
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
