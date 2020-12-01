# spring-boot-template

* temp links
  * https://www.baeldung.com/java-read-pem-file-keys
  * https://medium.com/@angela.amarapala/sending-email-confirmation-for-account-activation-with-spring-java-cc3f5bb1398e

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
## Certificates
```bash
openssl genrsa -out private_key.pem 4096
openssl rsa -pubout -in private_key.pem -out public_key.pem

# convert private key to pkcs8 format in order to import it from Java
openssl pkcs8 -topk8 -in private_key.pem -inform pem -out private_key_pkcs8.pem -outform pem -nocrypt

```
