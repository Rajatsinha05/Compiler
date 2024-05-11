# Use the official Ubuntu 20.04 LTS image as a base
FROM ubuntu:20.04

# Update package lists
RUN apt-get update

# Install openjdk-11-jdk
RUN apt-get install -y openjdk-11-jdk

# Install JDK 17
RUN apt-get install -y openjdk-17-jdk

# Install nodejs and npm
RUN apt-get install -y nodejs npm

# Install gcc
RUN apt-get install -y gcc

# Install g++
RUN apt-get install -y g++

# Remove package lists to reduce image size
RUN rm -rf /var/lib/apt/lists/*

# Set the working directory inside the container
WORKDIR /app

# Copy the entire project directory into the container
COPY . .

# Expose any ports the app is listening on
EXPOSE 8080

# Define the command to run your application using Gradle's bootRun
CMD ["./gradlew", "bootRun"]
