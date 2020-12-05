FROM gradle:6.6.1-jdk11

ENV JAVA_OPTS=""

COPY ./build/libs/*.jar /app.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]
