FROM openjdk

WORKDIR /app

COPY target/planets-0.0.1-SNAPSHOT.jar /app/api-monnos-start.jar

ENTRYPOINT ["java", "-jar" , "api-monnos-start.jar"]