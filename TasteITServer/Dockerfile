FROM openjdk:21
LABEL authors="jakubkonkol"

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

CMD ["./mvnw", "spring-boot:run"]
