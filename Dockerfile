FROM eclipse-temurin:8u372-b07-jre-alpine
WORKDIR /service

# Artifact
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose internal port
EXPOSE 4000/tcp

# Image entrypoint
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /service/app.jar ${@}"]