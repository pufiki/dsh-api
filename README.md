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

API details:

- [POST] **/register/contractor** - Register Contractor.
Contractor (Customer is the same but without 'specialization'):
```json
{   
    "companyName":"test",
    "email":"test",
    "password":"test",
    "specialization":"test"
}
```
- [GET] **/login/contractor** - Login as a Contractor.
```json
{   
    "email":"test",
    "password":"test"
}
```

- [GET] **/restapi/contractor** -> returns all Contractors (also, have *size*, *page*, and *sort* params)
- [DELETE] **/restapi/contractor/{id}** -> deletes Contractor with {id}.

Same requests work with **../customer**, **/restapi/customers** and **/restapi/workRequests/**.

Work Request:
```json
{   
    "name":"test",
    "description":"test"
}
```