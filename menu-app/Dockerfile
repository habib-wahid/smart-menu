# Build stage
FROM gradle:8.12-jdk21 AS build

WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the application
RUN gradle build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jdk-alpine-3.23

WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/build/libs/menu-app-0.0.1-SNAPSHOT.jar app.jar

# Create uploads directory for file storage
RUN mkdir -p /app/uploads/files

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]