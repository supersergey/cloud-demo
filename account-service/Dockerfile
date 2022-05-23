FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar","--spring.datasource.url=${DATASOURCE_URL}","--spring.profiles.active=${SPRING_ACTIVE_PROFILE}"]