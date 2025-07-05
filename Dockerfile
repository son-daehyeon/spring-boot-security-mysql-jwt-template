FROM gradle:8.14.2-jdk21-alpine AS builder

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle . .

RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache tzdata

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod

COPY --from=builder /home/gradle/project/build/libs/*.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]