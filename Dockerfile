# Use Eclipse Temurin as base image with Java 21
FROM eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the Maven wrapper files
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the Maven wrapper executable
RUN chmod +x mvnw

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the application with web mode enabled
CMD ["java", "-jar", "-Dspring.main.web-application-type=SERVLET", "target/chatdm-0.0.1-SNAPSHOT.jar"] 