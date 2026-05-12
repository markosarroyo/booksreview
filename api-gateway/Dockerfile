# ── Stage 1: Build ───────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Cachear dependencias Maven antes de copiar el código fuente
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -q 2>/dev/null || true

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn package -DskipTests -q

# ── Stage 2: Runtime ──────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

# Usuario no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copiar el jar desde el stage de build
COPY --from=builder /app/target/api-gateway-*.jar app.jar

# Cambiar propietario
RUN chown appuser:appgroup app.jar

USER appuser

# Puerto de la aplicación
EXPOSE 8080

# Healthcheck para orquestadores
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
    CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# JVM optimizada para contenedores:
# - UseContainerSupport: detecta límites de CPU/memoria del contenedor
# - MaxRAMPercentage: usa hasta el 75% de la RAM del contenedor
# - ExitOnOutOfMemoryError: evita estados inconsistentes
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
