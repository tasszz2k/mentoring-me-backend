# The image we are creating is based on a Java 11 OpenJDK image.
FROM openjdk:11-jre-slim-buster

# This sets the environmental variable.
ENV TZ=Asia/Ho_Chi_Minh
ENV PORT=8080
ENV APP_HOME=/app
ARG JAR_FILE=build/libs/*.jar


# This set timezone
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Make directory for application
RUN mkdir -p /app

# This is the directory where the output of CMD should run
WORKDIR $APP_HOME

# This copies the file or a directory to the container’s directory.
# Thus, copy build/libs/*.jar to the container’s app.jar.
COPY $JAR_FILE $APP_HOME/dist/mentoring-me-1.0.jar

# Expose sets the port number to connect to the host.
EXPOSE $PORT

# When the container is created using docker run or gets started using docker start, it runs this command
#The format is CMD ["fileToRun", "param1", "param2"]
CMD ["java", "-jar", "/app/dist/mentoring-me-1.0.jar"]
