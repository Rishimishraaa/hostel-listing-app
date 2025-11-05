# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy the entire project
COPY . .

# Move into your Spring Boot app directory
WORKDIR /app/Online-Hostel-Listing-app

# Build the Spring Boot JAR
RUN mvn clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the JAR file
CMD ["java", "-jar", "target/Online-Hostel-Listing-app-0.0.1-SNAPSHOT.jar"]
