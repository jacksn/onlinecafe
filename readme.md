[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2b368e5d8b104e72883fc0887cf38536)](https://www.codacy.com/app/gml-jackson/onlinecafe?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jacksn/onlinecafe&amp;utm_campaign=Badge_Grade)
[![Build Status](https://travis-ci.org/jacksn/onlinecafe.svg?branch=master)](https://travis-ci.org/jacksn/onlinecafe)
[![codecov](https://codecov.io/gh/jacksn/onlinecafe/branch/master/graph/badge.svg)](https://codecov.io/gh/jacksn/onlinecafe)

# Online cafe

This is a test task Java web application that uses multitier architecture.

All versions use:
* Plain JDBC for database connectivity.
* Logback for logging, JUnit, Hamcrest, Mockito for testing.
* JSP views, Boostrap, jQuery, Bootstrap Notify, Font Awesome for front-end.

Current version uses Spring Boot, Spring Web MVC, Spring Data JPA, Hibernate ORM, Hibernate Validator, Spring Boot Test.

Links to other versions:   
* [Spring Web MVC, Spring IoC, Hibernate ORM, Hibernate Validator, Spring Test](https://github.com/jacksn/onlinecafe/tree/spring-webmvc).
* [Servlet API, Spring IoC, Hibernate ORM, Hibernate Validator, Spring Test](https://github.com/jacksn/onlinecafe/tree/hibernate).
* [Servlet API, Spring IoC, Spring Test](https://github.com/jacksn/onlinecafe/tree/spring_ioc).
* [Servlet API](https://github.com/jacksn/onlinecafe/tree/jdbc-servlet-jsp).

## Configuration

By default application will read database connection properties from
[resources/db/db.properties](https://github.com/jacksn/onlinecafe/blob/master/src/main/resources/db/db.properties) file.
You can override default database connection properties by creating separate properties file under ```resources/db``` directory
(e.g. db_mssql.properties) and setting system property ```db_config_path``` pointing to that file.

Available jdbc connectors: MySQL, PostgreSQL, MSSQL, HSQLDB.

Also you can change repositories implementations and discount strategy.

You can choose which repositories implementations to use by activating one from following Spring profiles:
* ```repo-jdbc``` for [plain jdbc implementation](https://github.com/jacksn/onlinecafe/tree/master/src/main/java/test/onlinecafe/repository/jdbc)
* ```repo-jpa``` for [Hibernate implementation](https://github.com/jacksn/onlinecafe/tree/master/src/main/java/test/onlinecafe/repository/jpa)
* ```repo-datajpa``` for [Spring Data JPA implementation](https://github.com/jacksn/onlinecafe/tree/master/src/main/java/test/onlinecafe/repository/datajpa)

You can choose which discount implementation to use by activating one from following Spring profiles:
* ```discount-none``` for [no-discount implementation](https://github.com/jacksn/onlinecafe/tree/master/src/main/java/test/onlinecafe/util/discount/NoDiscount.java)
* ```discount-simple``` for [simple discount implementation](https://github.com/jacksn/onlinecafe/tree/master/src/main/java/test/onlinecafe/util/discount/SimpleDiscount.java)

By default application will use ```repo-datajpa``` and ```discount-none``` Spring profiles.

## How to Run 

This application is packaged as a war which has Tomcat 8 embedded. No Tomcat installation is necessary.

* Clone this repository 
* Make sure you are using JDK 1.8 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run application with following command:
```
    mvn spring-boot:run -Drun.arguments="--spring.profiles.active=repo-jpa,--spring.profiles.active=discount-simple,--db_config_path=db_postgres.properties"
```