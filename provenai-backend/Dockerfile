FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Install git
RUN apk update && apk add git maven

# Set build arguments for Git repository and token
ARG PROVEN_AI_GIT_REPOSITORY
ARG PROVEN_AI_GIT_BRANCH

# Clone the private repository using the token
RUN git clone -b ${PROVEN_AI_GIT_BRANCH} https://${PROVEN_AI_GIT_REPOSITORY} /proven-ai

# Navigate to the private repository directory and run Maven install
WORKDIR /proven-ai/provenai-sdk
RUN mvn clean install -DskipTests

WORKDIR /app

# Copy only the POM file to download dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src src

# Build the application
RUN mvn clean install -DskipTests

# Final image
#FROM eclipse-temurin:21-jdk
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp

# Copy the built JAR file from the builder stage
COPY --from=builder /app/target/*.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]

