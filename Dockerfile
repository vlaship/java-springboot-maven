### Build stage
# Builder maven
FROM maven:3-eclipse-temurin-21-alpine AS builder

# Set the working directory inside the container
WORKDIR /tmp

# Copy the source code into the container
COPY pom.xml .
COPY src src/

# Build
RUN mvn clean package -DskipTests

# Extract the layers
RUN java -Djarmode=layertools -jar target/*.jar extract

### Run stage
# Create a minimal production image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Set the working directory inside the container
COPY --from=builder /tmp/dependencies/ ./
COPY --from=builder /tmp/snapshot-dependencies/ ./
COPY --from=builder /tmp/spring-boot-loader/ ./
COPY --from=builder /tmp/application/ ./

# Run the binary when the container starts
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
