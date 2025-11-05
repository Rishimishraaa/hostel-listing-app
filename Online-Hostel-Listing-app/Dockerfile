# Use official OpenJDK 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy only Maven wrapper and pom.xml first (for caching)
COPY Online-Hostel-Listing-app/mvnw Online-Hostel-Listing-app/.mvn/ pom.xml ./

# Copy the entire project
COPY . .

# Move into the app folder
WORKDIR /app/Online-Hostel-Listing-app

# Give permission to mvnw
RUN chmod +x mvnw

# Build the app
RUN ./mvnw clean package -DskipTests

# Expose Spring Boot port
EXPOSE 8080

# Run the Spring Boot JAR
CMD ["java", "-jar", "target/Online-Hostel-Listing-app-0.0.1-SNAPSHOT.jar"]
