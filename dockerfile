FROM openjdk:17-jdk
COPY target/tp-foyer-5.0.0.jar .
EXPOSE 8089
ENTRYPOINT ["java","-jar","tp-foyer-5.0.0.jar"]
