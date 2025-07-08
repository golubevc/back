FROM openjdk:22-jdk-slim

# Установка необходимых пакетов
RUN apt-get update && apt-get install -y \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Создание рабочей директории
WORKDIR /app

# Копирование Maven wrapper и pom.xml
COPY mvnw mvnw.cmd pom.xml ./
COPY .mvn .mvn

# Копирование исходного кода
COPY src ./src

# Сборка приложения
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Экспонирование порта
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Запуск приложения
CMD ["java", "-jar", "-Dspring.profiles.active=test", "target/anecole-backend-1.0.0.jar"] 