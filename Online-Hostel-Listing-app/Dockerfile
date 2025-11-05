# Use an official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Go inside your app folder
WORKDIR /app/Online-Hostel-Listing-app

# Give permission and build
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot JAR
CMD ["java", "-jar", "target/*.jar"]
