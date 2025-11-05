#!/bin/bash
set -e

# ✅ Java setup
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# ✅ Maven wrapper permissions
chmod +x mvnw

# ✅ Build the project
./mvnw clean package -DskipTests

# ✅ Run the JAR file
java -jar target/*.jar
