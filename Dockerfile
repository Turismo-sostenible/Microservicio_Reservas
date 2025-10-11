# --- ETAPA 1: Build ---
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar pom.xml y descargar dependencias (sin barra de progreso)
COPY pom.xml .
RUN mvn dependency:go-offline --no-transfer-progress

# Copiar el resto del proyecto
COPY . .

# Compilar y empaquetar sin ejecutar ni compilar tests
RUN mvn clean package -Dmaven.test.skip=true --no-transfer-progress

# --- ETAPA 2: Run ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiar el JAR compilado
COPY --from=builder /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]