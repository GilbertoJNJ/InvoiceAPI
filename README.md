# Store-API
## :book:Summary 
* [1. Description](#description)
* [2. Technologies](#technologies)
* [3. Dependencies](#dependencies)
* [4. Prerequisites](#prerequisites)
* [5. Install](#install)
* [6. How To Use](#how-to-use)

## Description
:trophy: Lists all the products of an establishment and searches by name;

:trophy: Do sales calculation.

## Technologies
- <img src="https://img.shields.io/static/v1?label=java&message=language&color=red&style=for-the-badge&logo=java"/>
- <img src="https://img.shields.io/static/v1?label=gradle&message=build&color=darkgreen&style=for-the-badge&logo=gradle"/>
- <img src="https://img.shields.io/static/v1?label=docker&message=container&color=cyan&style=for-the-badge&logo=docker"/>
- <img src="https://img.shields.io/static/v1?label=Postgres&message=database&color=blue&style=for-the-badge&logo=postgresql"/>
- <img src="https://img.shields.io/static/v1?label=spring&message=framework&color=green&style=for-the-badge&logo=spring"/>
- <img src="https://img.shields.io/static/v1?label=junit&message=tests&color=darkgreen&style=for-the-badge&logo=junit5"/>
- <img src="https://img.shields.io/static/v1?label=heroku&message=deploy&color=purple&style=for-the-badge&logo=heroku"/>

## Dependencies
 - Spring Data JPA
 - Spring Web
 - Spring Boot DevTools
 - Spring Test
 - [Lombok](https://projectlombok.org/)
 - [PostgreSQL](https://www.postgresql.org/)

## Prerequisites
### Docker
:warning: Create Postgres database:
```shell script
docker run --name products-db -d -p 5432:5432 -e POSTGRES_USER=postgres_user_product -e POSTGRES_PASSWORD=super_password -e POSTGRES_DB=products postgres
```

:warning: Populate database:
```shell script
cd ~\workspace\products-api\src\main\resources\PostgreSQL

Linux:
docker run -it --rm --net=host -v $PWD:/tmp postgres /bin/bash

Windows:
docker run -it --rm --net=host -v "%cd%":/tmp postgres /bin/bash

psql -h localhost -U postgres_user_product products -f /tmp/produtos.sql
psql -h localhost -U postgres_user_product products -f /tmp/categoria.sql

psql -h localhost -U postgres_user_product products
```

## Install 
1. In the terminal, clone the project:
```shell script
git clone https://github.com/GilbertoJNJ/Store-API.git
```

2. Enter in the projet diretory:
```shell script
cd ~\store-api
```

3. Execute the command:
```shell script
gradle bootrun
```

To run the tests:
```shell script
gradle clean test
```

## How To Use
1. List all the products:
``` shell script
http://localhost:8080/products
```

2. List by name:
``` shell script
http://localhost:8080/products/[name]
```

3. Calculation of sales:
``` shell script
http://localhost:8080/sales?n=[quantity]&name=[name]
```

4. Clean sales:
``` shell script
http://localhost:8080/sales/clean
```
