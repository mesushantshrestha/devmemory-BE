# -------- Build stage --------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copy Gradle wrapper + build files first (better caching)
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle* settings.gradle* ./

# Pre-download dependencies (cacheable layer)
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies

# Copy source
COPY src ./src

# Build jar (skip tests for faster CI; change if you want tests)
RUN ./gradlew --no-daemon clean bootJar -x test

# -------- Run stage --------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the built jar
COPY --from=build /app/build/libs/*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
