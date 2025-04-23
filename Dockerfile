FROM gradle:8.4-jdk17 AS build
COPY . /home/app
WORKDIR /home/app
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /home/app/build/libs/Flo_Usr_Demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]