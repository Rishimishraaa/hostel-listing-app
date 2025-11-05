#!/bin/bash
set -e

# Set JAVA_HOME (Railway environment)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# Go inside the app folder
cd Online-Hostel-Listing-app

# Give permission to mvnw
chmod +x mvnw

# Build project
./mvnw clean package -DskipTests

# Run the JAR file
java -jar target/*.jar
