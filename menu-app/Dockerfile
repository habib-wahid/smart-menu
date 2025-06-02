FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR file to the container
# Replace 'menu-app-0.0.1-SNAPSHOT.jar' with your actual JAR file name
COPY build/libs/menu-app-0.0.1-SNAPSHOT.jar app.jar

# Create uploads directory for file storage
RUN mkdir -p /app/uploads/files

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]