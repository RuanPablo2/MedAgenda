FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY . .
RUN mvn clean package -pl med-gateway -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/med-gateway/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]