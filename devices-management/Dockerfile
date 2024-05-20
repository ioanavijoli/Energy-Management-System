# Use the official Maven image as the builder
FROM maven:3.8.3-openjdk-17 AS builder

# Copy your application source code to the container
COPY ./src/ /root/src
COPY ./pom.xml /root/ 
WORKDIR /root

# Build the application using Maven
RUN mvn package
RUN java -Djarmode=layertools -jar /root/target/users_management-1.0-SNAPSHOT.jar list
RUN java -Djarmode=layertools -jar /root/target/users_management-1.0-SNAPSHOT.jar extract
RUN ls -l /root

# Create a new image based on a minimal JRE image
FROM openjdk:17-jdk-slim-buster

# Set environment variables
ENV TZ=UTC
ENV DB_IP=localhost
ENV DB_PORT=5432
ENV DB_USER=postgres
ENV DB_PASSWORD=root
ENV DB_DBNAME=devices_management

# Copy dependencies from the builder stage
COPY --from=builder /root/dependencies/ ./
COPY --from=builder /root/snapshot-dependencies/ ./

# Add a delay for any required setup
RUN sleep 10

# Copy the Spring Boot loader and application code
COPY --from=builder /root/spring-boot-loader/ ./
COPY --from=builder /root/application/ ./

# Define the entry point for your Spring Boot application
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher", "-XX:+UseContainerSupport", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:MaxRAMFraction=1", "-Xms512m", "-Xmx512m", "-XX:+UseG1GC", "-XX:+UseSerialGC", "-Xss512k", "-XX:MaxRAM=72m"]