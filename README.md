### Problem Statement
Create a simple application which checks if a word is a palindrome. The application should have a simple REST API with the following capability:
- Submit/post messages
- List received messages
- Retrieve a specific message and determine if it is a palindrome
- Delete a specific message

### Overview
A backend service (Message-Service, Auth Service) using Java and Spring Boot divided into three layers REST (Controller), business (Service) and data access (Repository) layers with basic CRUD operation. 

#### The architecture is composed by four services:

![test drawio (2)](https://user-images.githubusercontent.com/13719978/175897850-c3402d68-5c09-45d4-83fd-ea77d3f18a70.png)

* `eureka-server` Service Discovery Server created with Eureka which allows services to find and communicate with each
  other without hard-coding the hostname and port.
* `api-gatway-service`: API Gateway created with Spring Cloud Gateway that uses the discovery-service to send the
  requests to the services.
* `auth-service`: Simple REST service created with Spring Boot, Spring Cloud Oauth2, Spring Data JPA, PostgresSql to
  use as an authorization service.
* `message-service`: Simple REST service created with Spring Boot, Spring Data JPA, PostgresSql to use as a resource
  service.
  
### Tools you will need

* Build tool : Maven
* Your favorite IDE but we will recommend `IntelliJ IDEA 2022.1.3`.
* PostgresSQL
* JDK 17+

### Code Structure by layers design

A way to place the classes is by layer. By following this structure all controllers can be placed in controllers package and services under services package and all entities under domain or model etc.

com \
&emsp;+- qlik \
&emsp;&emsp;    +- assignment \
&emsp;&emsp;&emsp;        +- MyApplication.java \
&emsp;&emsp;&emsp;            +- controller \
&emsp;&emsp;&emsp;            +- domain \
&emsp;&emsp;&emsp;            +- exception \
&emsp;&emsp;&emsp;            +- repository \
&emsp;&emsp;&emsp;            +- security\
&emsp;&emsp;&emsp;            +- service\
&emsp;&emsp;&emsp;            +- util


### Microservice Running Process:

- Install Docker Compose https://docs.docker.com/compose/install/. If you installed Docker Desktop/Toolbox for either Windows or Mac, you already have Docker Compose ignore this step
- run `docker compose up`. This tool is useful when running the multiple-container Docker Application.
- Inside this docker compose file, we are running postgresSql, PgAdmin, EurekaServer, AuthService, MessageService.
 After the docker compose successfully ran. Here is url to access the service
   
   Service Name | URL                                 | Service Description 
    --- |-------------------------------------| ---
   eureka server | http://localhost:8761/              | Eureka Server is an application that holds the information about all client-service applications. Every Micro service will register into the Eureka server and Eureka server knows all the client applications running on each port and IP address. Eureka Server is also known as Discovery Server
   Api Gatway | http://localhost:8080/              | Whenever we think of microservices and distributed applications, the first point that comes to mind is security.Obviously, in distributed architectures, it is really difficult to manage security as we do not have much control over the application. So in this situation, we always need to have a central entry point to this distributed architecture. This is the reason why, in microservices, we have a separate and dedicated layer for all these purposes. This layer is known as the API Gateway. It is an entry point for a microservice's architecture.
   Auth Service | http://localhost:8080/api/message   | Api Docs : http://localhost:8080/api/message/api-docs,\ Swagger-UI http://localhost:8080/api/message/swagger-ui.html .\Authorization server to authenticate your identity to provide access_token, which you can use to request data from resource server
   Message Service | http://localhost:8080/auth/user     |Api Docs : http://localhost:8080//auth/user/api-docs\, Swagger-UI http://localhost:8080//auth/user/swagger-ui.html\ Create a simple application which checks if a word is a palindrome. The application should following capabilities: Submit/post messages,List received messages,Retrieve a specific message and determine if it is a palindrome, Delete a specific message
   PGAdmin | http://localhost:16543     | Login/Password : test@gmail.com/test123. Create a server and use hostame/user/password : postgres/user/admin

Note : We have a script which will inject at volume of Postgres Container to create the database used by Auther Service and Message Service

### Supported Feature in Microservices
- Distributed Tracing using Spring Cloud Slueth
- Documenting a microservice Using OpenAPI 3.0 
- Error Handling
- Authorization framework Oauth JWT using Spring security
- DDL Creation using JPA Hbernate
- Basic Unit Test - Controller Layer, Service Layer, Repository Layer added
  
### Good to have feature

#### Application
- Microservice CQRS pattern can be handful to segregate read and write queries. To support this we would require the read-replica instance of Postgres SQL master instance. Offcourse this pattern needs to implement based on traffic we are expecting for read and write queries
- Health Check
- Monitoring based on various resource consumption factors
- Log based Alerts Policy and notify on slack/email to stakeholder
- Http Status based Alerts Policy. eg. Alerting the unAuthorize request
- BDD test cases this approach will cover from bussiness point view

#### Automation and Release Management
- Github action to define deployement worflow
- Integration with Sonar for code-quality check.
- Integration with Black Duck to find any vulnerbilities in dependencies used in the project 
- Build and Push all the docker in Artifact Registry
- Once the service is deployed on any cloud platform. or managed Kubernetes, good to have smoke testing on the service. Once the testing is pass then migrate 100% traffic to service
- Rollback of deployement to specific version

#### Known Issue
- AuthService : Authorization based on user roles
