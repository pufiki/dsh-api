Run application locally with runtime h2 database 
(no need for manual configuration).
```bash
./mvnw spring-boot:run 
 ```
 
 Run application with configuration set for production. 
 The remote PostgreSQL database should be up and running.
 The configurations file src/main/resources/application.yml.
 ```
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
``` 