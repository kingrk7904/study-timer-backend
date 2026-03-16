# ---------- STAGE 1 : Build ----------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /build

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# ---------- STAGE 2 : Run ----------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

# Azure App Service injects PORT at runtime; default to 8080 locally.
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]