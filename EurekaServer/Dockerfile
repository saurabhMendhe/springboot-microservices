FROM maven:3.8.4-openjdk-17 as eureka-server-build
WORKDIR /eureka-server
COPY . /eureka-server
RUN mvn clean install -DskipTests


FROM openjdk:17-jdk
WORKDIR /eureka-server
COPY --from=eureka-server-build eureka-server/target/EurekaServer-0.0.1-SNAPSHOT.jar ./eureka-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]

