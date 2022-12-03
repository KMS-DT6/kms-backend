FROM openjdk:11

WORKDIR /app

COPY target/kms-backend.jar kms-backend.jar

ENTRYPOINT ["java", "-jar", "kms-backend.jar"]

EXPOSE 5000