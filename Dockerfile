# Use JDK 21 with Alpine
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory inside the container
WORKDIR /acme-air

# Copy the JAR file into the container
COPY build/libs/*-SNAPSHOT.jar booking-0.0.1.jar

# Expose port (optional)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "booking-0.0.1.jar"]