# spring-boot-template

* temp links
  * https://www.baeldung.com/java-read-pem-file-keys

Spring boot dockerized service template with an example.

Loosely based on https://dzone.com/articles/spring-boot-2-restful-api-documentation-with-swagg

# Usage with Docker:
## Prerequisites:
  * Docker installed
  * Docker running
## Usage:
* To start the app
  ````
  docker-compose up
  ````
* To build a Docker image: in root folder run 
  ````
  docker build .
  ````
* Swagger is accessible here:
   * Docker Desktop:
      ```` 
      http://localhost:3011/swagger-ui.html
      ````
   * Docker toolbox:
     ````
     http://192.168.99.100:3011/swagger-ui.html
     ````

# Usage without Docker:
* Swagger is accessible here (once the app is started):
  ````
  http://localhost:8080/swagger-ui.html
  ````
