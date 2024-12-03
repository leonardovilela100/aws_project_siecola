# Etapa 1: Construir o JAR
FROM gradle:8.0-jdk17 AS builder

WORKDIR /home/gradle/project

COPY . .

RUN gradle clean bootJar

# Etapa 2: Criar a imagem final
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

ARG IMAGE_VERSION
LABEL version=$IMAGE_VERSION

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
