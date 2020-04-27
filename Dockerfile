FROM openjdk:11-jre
COPY build/libs/*.jar /app/org.flyak.api.jar

CMD ["java", "-jar", "/app/org.flyak.api.jar"]

EXPOSE 907