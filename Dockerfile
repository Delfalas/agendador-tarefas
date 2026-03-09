FROM gradle:8.7-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/agendador-tarefas.jar
EXPOSE 8081
CMD ["java", "-jar", "/app/agendador-tarefas.jar"]