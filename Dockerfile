# Build Stage
FROM maven:3.8.5-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml /app
COPY src /app/src

ENV PORT=8080

RUN mvn clean package -DskipTests


# Deploy Stage
FROM eclipse-temurin:21.0.3_9-jre-alpine

ARG jar_file=springpetclinic.jar

RUN adduser -D Docker_User

USER Docker_User

WORKDIR /home/Docker_User

COPY --from=build /app/target/*.jar /home/Docker_User/${jar_file}

HEALTHCHECK --interval=30s --timeout=30s --start-period=5s --retries=3 \
CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

VOLUME ["/data"]

ENTRYPOINT ["java","-jar"]

CMD ["springpetclinic.jar"]