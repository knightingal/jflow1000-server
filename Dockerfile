FROM eclipse-temurin:11
RUN mkdir /opt/app
COPY build/libs/JFlow1000Server-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/JFlow1000Server-0.0.1-SNAPSHOT.jar"]