# HMCTS Dev Test Backend
To build the backend project run the command:
$ ./gradlew clean bootJar

To run the backend project run:

$ java -jar build/libs/test-backend.jar

To run it using Docker run:

$ docker-compose up --build

To access the API documentation using the Swagger UI open: http://localhost:4000/api/swagger-ui

To access the API documentation as an OpenAPI JSON open: http://localhost:4000/api/docs
