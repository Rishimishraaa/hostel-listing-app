# Use an official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy everything from host to container
COPY . .

# Move inside your Spring Boot project folder
WORKDIR /app/Online-Hostel-Listing-app

# Give permission to mvnw and build the app
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the Spring Boot JAR file
CMD ["java", "-jar", "target/Online-Hostel-Listing-app-0.0.1-SNAPSHOT.jar"]
