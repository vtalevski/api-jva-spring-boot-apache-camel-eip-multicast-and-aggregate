FROM openjdk:latest

LABEL author = "Viktor Talevski"

ADD ./target/api-jva-spring-boot-scientist-personal-life-module.jar ./api-jva-spring-boot-scientist-personal-life-module.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "./api-jva-spring-boot-scientist-personal-life-module.jar"]
