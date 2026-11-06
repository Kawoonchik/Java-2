# Використовуємо офіційний образ Java 21
FROM eclipse-temurin:21-jre-alpine
# Робоча директорія всередині контейнера
WORKDIR /app
# Копіюємо зібраний JAR-файл із папки target
COPY target/core-service-1.0.0-SNAPSHOT.jar app.jar
# Відкриваємо порт
EXPOSE 8081
# Команда для запуску сервісу
ENTRYPOINT ["java", "-jar", "app.jar"]