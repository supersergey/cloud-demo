FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar",\
"--gateway.services.service-a.baseUri=${SERVICE_A_BASE_URI}",\
"--gateway.services.service-b.baseUri=${SERVICE_B_BASE_URI}"]