# Imagen base con Java 21 (Eclipse Temurin)
FROM eclipse-temurin:21-jdk-alpine

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo JAR generado al contenedor
COPY target/kafka-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto donde corre Spring Boot
EXPOSE 8888

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]