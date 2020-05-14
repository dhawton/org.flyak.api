FROM openjdk:11-jre
COPY build/libs/*.jar /app/org.nzvirtual.api.jar
COPY build/libs/gitinfo /app/gitinfo

CMD ["java", "-jar", "/app/org.nzvirtual.api.jar"]

EXPOSE 907