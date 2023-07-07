**Software requirements**
------------------------------------------------------------------------------------
* Java 17
* Maven
* PostgreSQL
* Postman

**Getting started**
------------------------------------------------------------------------------------
Before starting the application, substitute your values in application.properties file.
spring.datasource.url=jdbc:postgresql://localhost:5432/phonecontacts
spring.datasource.username=${login}
spring.datasource.password=${password}

**Description**
------------------------------------------------------------------------------------
We have one registered user with credentials: login = "admin", password = "admin".
To register new eser please use POST method for localhost:8080/registration and provide login and password.
After registration please use POST method for localhost:8080/auth and provide login and password.
For sign out from application use GET method localhost:8080/logout.

To see **all your contacts** use GET for localhost:8080/contacts.

To **add new contact** use POST for localhost:8080/contacts and provide json with data, for instance:
    {
        "name": "contact",
        "emails": ["email1@gmail.com", "email2@gmail.com"],
        "phoneNumbers": ["+380980000000", "+380981111111"]
    }

To **update contact** use PUT for localhost:8080/contacts and provide json with data, for instance:
    {
    "name": "contact",
    "emails": ["email1@gmail.com", "email3@gmail.com"],
    "phoneNumbers": ["+380980000000", "+380982222222"]
    }

To **delete contact** use DELETE for localhost:8080/contacts and provide query parameter for name.

To **export all your contact** to main directory use GET for localhost:8080/contacts/export.