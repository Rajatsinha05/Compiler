# Stage 1: Build
FROM gradle:7.5.1-jdk17 as build

# Set the working directory
WORKDIR /app

# Copy only the Gradle wrapper and build files to cache dependencies
COPY gradlew /app/gradlew
COPY gradle /app/gradle
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle

# Ensure Gradle wrapper has executable permissions
RUN chmod +x gradlew

# Fetch dependencies to cache them
RUN ./gradlew dependencies --no-daemon

# Copy the rest of the project files into the container
COPY . .

# Run the Gradle build to create the jar file
RUN ./gradlew bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-jammy

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/Compiler-0.0.1-SNAPSHOT.jar ./app.jar

# Expose port 8090
EXPOSE 8090

# Command to run the application
CMD ["java", "-jar", "app.jar"]
