FROM eclipse-temurin:21
LABEL maintainer="Vini Basilio"
WORKDIR /app
COPY target/simplificado-0.0.1-SNAPSHOT.jar /app/pagamentos-simplificado
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "pagamentos-simplificado"]