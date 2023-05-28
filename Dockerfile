# Stage 1
FROM maven:3.9.2-eclipse-temurin-17-alpine AS mavenBuilder
WORKDIR /application

# Copy codebase
COPY . .

# Build artifact
RUN mvn clean package -Dmaven.test.skip=true

# Stage 2
FROM eclipse-temurin:8u372-b07-jre-alpine AS builder
WORKDIR /application

# Copy artifact from builder
COPY --from=mavenBuilder application/target/*.jar ./app.jar

# Layertools extract builder
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3
FROM eclipse-temurin:8u372-b07-jre-alpine
WORKDIR /service

# Copy artifact from builder
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# Expose internal port
EXPOSE 4000/tcp

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.JarLauncher"]