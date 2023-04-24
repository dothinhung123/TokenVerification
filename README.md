# TokenVerification 
**Token-based Authentication with JWT, Spring, Java 11**
## Table of contents
* [General info](#general-info)
* [Database Structure](#database-structure)
* [Usecases](#usecases)
* [Technologies](#technologies)
* [Setup](#setup)
* [Notes](#notes)
* [References](#references)
## General info
 Token-base Authentication is created for make sure that security for Authentication. 
## Database Structure 
![image](https://user-images.githubusercontent.com/54989905/233968743-c29129b7-3029-43a4-8a69-9c2c6efe6cb5.png)
## Usecases </br>
   **AUTHENTICATION** </br>
- [ ] User sign up with application , including some basic information </br>
       **username** : must be an email </br>
       **password**  : will be encrypted when store in to databaseand other infor if have </br>
- [ ] After finished sign up ,Application will send confirmation email to the email that user alreadly provide </br>
- [ ] User now can click to the Link to Active Account </br>
- [ ] After finished sign up , now user will be able to login to the application </br>
## Technologies
* Java 11
* Spring boot 2.7.5
* MySQL 5
* Spring Security
* lombok
* nimbus-jose-jwt 9.22 ( support for JWS (JSON Web Signature) AND JWE ( JSON Web Encrypted)
## Setup
* **Choose port** </br> 
>server.port =
* **Open project and set up database**  </br>
>spring.datasource.url=jdbc:mysql://localhost/ </br>
 spring.datasource.username=  </br>
 spring.datasource.password=  </br>
 spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect  </br>
 spring.jpa.hibernate.ddl-auto=update  </br>
* **Fill Owner Email ( this email seems like business email or system email , we will use it to send email token to the others )** </br>
>spring.mail.username= </br>
spring.mail.password= </br>

## Notes
## References</br>
* Spilca, L.(2020) . Spring Security in Action
* BezKoder. (2023). Spring Boot Token based Authentication with Spring Security & JWT </br>
  from https://www.bezkoder.com/spring-boot-jwt-authentication/

