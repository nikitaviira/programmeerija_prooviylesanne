FROM amazoncorretto:21 AS build
WORKDIR /app
COPY ./ /app
RUN ./gradlew build -x test

# Runtime stage
FROM amazoncorretto:21 AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
CMD ["java", "-jar", "/app/app.jar"]