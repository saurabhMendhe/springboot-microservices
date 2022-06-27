FROM maven:3.8.4-openjdk-17 as api-gateway-build
WORKDIR /api-gateway
COPY . /api-gateway
RUN mvn clean install -DskipTests


FROM openjdk:17-jdk
WORKDIR /api-gateway
COPY --from=api-gateway-build api-gateway/target/ApiGatewayService-0.0.1-SNAPSHOT.jar ./api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]

