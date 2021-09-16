package com.gilberto.storeapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String name) {
        super("Product not found with Name " + name);
    }
}
