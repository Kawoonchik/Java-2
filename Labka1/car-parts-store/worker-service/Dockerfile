# Використовуємо офіційний образ Java 21
FROM eclipse-temurin:21-jre-alpine
# Робоча директорія
WORKDIR /app
# Копіюємо зібраний JAR-файл воркера
COPY target/worker-service-1.0.0-SNAPSHOT.jar app.jar
# Відкриваємо порт воркера
EXPOSE 8082
# Команда для запуску
ENTRYPOINT ["java", "-jar", "app.jar"]