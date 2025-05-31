FROM openjdk:17-jdk-slim
WORKDIR /app
COPY lib/build/libs/lib.jar /app/libbbb.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "libbbb.jar"]