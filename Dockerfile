# Use Java 17
FROM eclipse-temurin:17-jdk

# App directory
WORKDIR /app

# Copy Maven build output
COPY target/*.jar app.jar

# Expose port (Render provides PORT env)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","app.jar"]
