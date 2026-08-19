FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY . .
RUN mvn clean package -pl med-appointment-service -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/med-appointment-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]