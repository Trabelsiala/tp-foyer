FROM openjdk:8
COPY target/tp-foyer-5.0.0.jar .
EXPOSE 8081
ENTRYPOINT ["java","-jar","tp-foyer-5.0.0.jar"]
