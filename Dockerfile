FROM gradle:8.12-jdk17-corretto AS builder
WORKDIR /usr/app
COPY build.gradle main.gradle settings.gradle gradle.properties lombok.config ./
COPY applications ./applications
COPY domain ./domain
COPY infrastructure ./infrastructure
RUN gradle bootJar --no-daemon -x test --info --stacktrace

FROM eclipse-temurin:17-jdk-alpine
ENV APP_HOME=/usr/app
ENV JAR_FILE=ConsumerApp.jar
ENV JAVA_OPTS='-XX:+UseContainerSupport -XX:MaxRAMPercentage=70'
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR $APP_HOME

COPY --from=builder $APP_HOME/applications/app-service/build/libs/*.jar $JAR_FILE
USER appuser

EXPOSE 9000

ENTRYPOINT ["sh","-c", "java $JAVA_OPTS -jar $JAR_FILE"]