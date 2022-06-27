FROM maven:3.8.4-openjdk-17 as message-service-build
WORKDIR /message-service
COPY . /message-service
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk
WORKDIR /message-service
COPY --from=message-service-build message-service/target/MessageService-0.0.1-SNAPSHOT.jar ./message-service.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "message-service.jar"]

