FROM adoptopenjdk/openjdk15

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# run command on startup
CMD ["echo", "Dockerfile demo"]