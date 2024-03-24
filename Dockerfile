#FROM  openjdk:18
#ARG JAR_FILE=target/*.jar
##COPY resources ./resources
#COPY ${JAR_FILE} /app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM tomcat:10.1.15

COPY ./target/AstonREST-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/
