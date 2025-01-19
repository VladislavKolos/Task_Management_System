FROM maven:3.8.7-amazoncorretto-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

RUN mvn clean package

RUN chmod -R 777 /app/logs

ENTRYPOINT ["mvn", "spring-boot:run"]
