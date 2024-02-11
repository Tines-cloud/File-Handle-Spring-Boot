#define base docker image

FROM openjdk:17
LABEL maintainer="tines"
ADD target/file-handle-spring-boot-0.0.1-SNAPSHOT.jar springboot-docker-image.jar
ENTRYPOINT ["java","-jar","springboot-docker-image.jar"]