# 1) Builder stage: build the jar with Gradle
FROM gradle:7.5-jdk17 AS builder
WORKDIR /home/gradle/project

# Copy only build scripts to leverage Docker layer cache
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/
RUN ./gradlew --version

# Copy source code and build the application (skip tests)
COPY . .
RUN ./gradlew clean build -x test --no-daemon

# 2) Runtime stage: minimal image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8080

# Set entrypoint to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]