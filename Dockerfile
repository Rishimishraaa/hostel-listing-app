FROM eclipse-temurin:17-jdk

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY . .

# Navigate to the actual Spring Boot project
WORKDIR /app/Online-Hostel-Listing-app

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/Online-Hostel-Listing-app-0.0.1-SNAPSHOT.jar"]
