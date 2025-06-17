FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw \
 && ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw package -DskipTests -B


# ----- THIS IS THE LINE THAT WAS CORRECTED -----
FROM openjdk:17-slim

WORKDIR /app

COPY --from=build /app/target/TaskManageService-*.jar task-service.jar

RUN apt-get update \
 && apt-get install -y curl \
 && rm -rf /var/lib/apt/lists/*

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "task-service.jar"]