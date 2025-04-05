# Usar imagem mínima do Java 22
FROM eclipse-temurin:22-jdk-alpine

# Criar diretório
WORKDIR /app

# Copiar o jar gerado
COPY target/desafio-itau-backend-1.0-SNAPSHOT.jar app.jar

# Comando para rodar o jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expor a porta 8080
EXPOSE 8080
