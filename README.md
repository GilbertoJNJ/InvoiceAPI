# Store-API
## :book:Summary 
* [1. Description](#description)
* [2. Technologies](#technologies)
* [3. Dependencies](#dependencies)
* [4. Integration](#integration)
* [5. Install](#install)
* [6. How To Use](#how-to-use)

## Description
:trophy: Generate invoices.

:trophy: Returns a paginated list of invoices.

:trophy: Search invoice by id.

:trophy: Cancel an invoice.

## Technologies
- <img src="https://img.shields.io/static/v1?label=java&message=language&color=red&style=for-the-badge&logo=java"/>
- <img src="https://img.shields.io/static/v1?label=gradle&message=build&color=darkgreen&style=for-the-badge&logo=gradle"/>
- <img src="https://img.shields.io/static/v1?label=Postgres&message=database&color=blue&style=for-the-badge&logo=postgresql"/>
- <img src="https://img.shields.io/static/v1?label=spring&message=framework&color=green&style=for-the-badge&logo=spring"/>
- <img src="https://img.shields.io/static/v1?label=junit&message=tests&color=darkgreen&style=for-the-badge&logo=junit5"/>
- <img src="https://img.shields.io/static/v1?label=aws&message=deploy&color=orange&style=for-the-badge&logo=amazonaws"/>

## Dependencies
 - Spring Data JPA
 - Spring Web
 - Spring Test
 - [Lombok](https://projectlombok.org/)
 - [Swagger](https://swagger.io/)

## Integration
This API has an integration with the LogiStockAPI to find products informations

- ✨ [LogiStockAPI](https://github.com/GilbertoJNJ/LogiStockAPI)


## Install 
1. In the terminal, clone the project:
```shell script
git clone https://github.com/GilbertoJNJ/InvoiceAPI.git
```

2. Enter in the projet diretory:
```shell script
cd ~\invoiceapi
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

Access documentation after building the project:
```
http://localhost:8080/swagger-ui.html
```
