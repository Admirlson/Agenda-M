FROM openjdk:21-jdk-slim
WORKDIR /agenda1
COPY target/*.jar agenda1-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","agenda1-0.0.1-SNAPSHOT.jar"]
