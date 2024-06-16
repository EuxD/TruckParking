FROM eclipse-temurin:17-jre-focal
#FROM ghcr.io/graalvm/graalvm-community:17


ENV MONGO_ADDRESS=172.31.6.11
ENV MONGO_PORT=27017



ADD target/Trucker-0.1.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]

