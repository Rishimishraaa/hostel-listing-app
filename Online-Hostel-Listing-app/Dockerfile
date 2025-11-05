# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory to root
WORKDIR /app

# Copy entire project
COPY . .

# Move into the actual Spring Boot subfolder
WORKDIR /app/Online-Hostel-Listing-app

# Build JAR
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "target/Online-Hostel-Listing-app-0.0.1-SNAPSHOT.jar"]
