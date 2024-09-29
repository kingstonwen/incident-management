# Use a base image with JDK 17
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file to the container
COPY target/incident-management-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
