### Build stage
# Builder maven
FROM scratch AS builder

# Set the working directory inside the container
WORKDIR /tmp

# Copy the source code into the container
COPY target/*.jar app.jar

# Extract the layers
RUN java -Djarmode=layertools -jar app.jar extract

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
