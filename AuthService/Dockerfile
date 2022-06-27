FROM maven:3.8.4-openjdk-17 as auth-service-build
WORKDIR /auth-service
COPY . /auth-service
RUN mvn clean install -DskipTests


FROM openjdk:17-jdk
WORKDIR /auth-service
COPY --from=auth-service-build auth-service/target/AuthService-0.0.1-SNAPSHOT.jar ./auth-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "auth-service.jar"]

