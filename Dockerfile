FROM frolvlad/alpine-oraclejdk8:slim
# ENV MONGO_URL mongodb://localhost/belfastjug_sample_01
EXPOSE 1010
RUN mkdir -p /app/
ADD target/e-recruit-0.0.1-SNAPSHOT.jar /app/e-recruit-01.jar
ENTRYPOINT ["java", "-jar", "/app/e-recruit-01.jar"]