# Etapa única - Usar jar já construído manualmente: mvn clean package
FROM eclipse-temurin:22-jre

WORKDIR /app

# Copia o JAR gerado manualmente
COPY ./target/desafio-itau-backend-1.0-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
