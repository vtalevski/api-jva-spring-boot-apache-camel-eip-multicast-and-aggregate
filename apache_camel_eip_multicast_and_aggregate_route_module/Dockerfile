FROM openjdk:latest

LABEL author = "Viktor Talevski"

ADD ./target/api-jva-spring-boot-apache-camel-eip-multicast-and-aggregate-route-module.jar ./api-jva-spring-boot-apache-camel-eip-multicast-and-aggregate-route-module.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "./api-jva-spring-boot-apache-camel-eip-multicast-and-aggregate-route-module.jar"]
