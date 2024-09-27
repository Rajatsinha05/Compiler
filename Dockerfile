# Use the official Gradle image as a base for the build stage
FROM gradle:7.6.1-jdk17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the build.gradle and settings.gradle files
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src ./src

# Build the application
RUN gradle build --no-daemon

# Use Ubuntu as the base image for the runtime stage
FROM ubuntu:22.04

# Avoid prompts from apt
ENV DEBIAN_FRONTEND=noninteractive

# Update package lists and install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jre-headless \
    nodejs \
    npm \
    gcc \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the built artifact from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "app.jar"]
