FROM openjdk:17-jdk-slim AS build

RUN apt-get update && apt-get install -y maven
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests
EXPOSE 8081
CMD ["java","-jar","target/employee-service-0.0.1-SNAPSHOT.jar"]



