FROM frolvlad/alpine-oraclejdk8:slim
EXPOSE 8080
RUN mkdir -p /app/
ADD target/unistore-api-0.0.1-SNAPSHOT.jar /app/unistore-api.jar
ENTRYPOINT ["java", "-jar", "/app/unistore-api.jar"]