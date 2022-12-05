FROM amazoncorretto:11
ENV TZ=Europe/Moscow
COPY target/*.jar main-server.jar
ENTRYPOINT ["java","-jar","/main-server.jar"]
