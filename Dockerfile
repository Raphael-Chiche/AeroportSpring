# ---------- Etape 1 : compilation du jar ----------
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# On copie d'abord le pom seul : tant qu'il ne change pas, Docker reutilise
# le cache des dependances et les builds suivants sont bien plus rapides
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -B dependency:go-offline

COPY src/ src/
RUN ./mvnw -q -B package -DskipTests

# ---------- Etape 2 : image finale, juste Java + le jar ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
