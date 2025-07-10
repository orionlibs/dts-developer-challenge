FROM gradle:8.6-jdk21 AS builder
WORKDIR /workspace

COPY gradlew settings.gradle* build.gradle* ./
COPY gradle ./gradle

RUN ./gradlew --no-daemon dependencies

# copy the rest of the source and build
COPY . .
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

COPY --from=builder /workspace/build/libs/test-backend.jar ./app.jar

EXPOSE 4000

ENTRYPOINT ["java", "-jar", "app.jar"]
