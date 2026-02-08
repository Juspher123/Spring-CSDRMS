# Stage 1: Build the application
FROM maven:3-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8080

# Environment variables with defaults
ENV PORT=8080
ENV JAVA_OPTS="-Xmx300m -Xss512k -XX:MaxRAMPercentage=75.0"

# Start the application
CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar app.jar"]
