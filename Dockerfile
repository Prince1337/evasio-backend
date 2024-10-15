FROM gradle:latest as builder  
  
# Lege das Arbeitsverzeichnis fest  
WORKDIR /app  
  
# Kopiere die build.gradle-Datei und den Quellcode in das Arbeitsverzeichnis  
COPY build.gradle .  
COPY src/ ./src/  

EXPOSE 8080
  
# Führe den 'bootJar'-Task aus, um das Spring Boot JAR zu erstellen  
RUN gradle bootJar --no-daemon  
  
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app  
# Kopiere das JAR aus dem vorherigen Builder-Image in das Arbeitsverzeichnis  
COPY --from=builder /app/build/libs/*.jar ./app.jar  
  
# Starte die Spring Boot-Anwendung  
CMD ["java", "-jar", "app.jar"]