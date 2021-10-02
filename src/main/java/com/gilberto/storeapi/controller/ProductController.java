package com.gilberto.storeapi.controller;

import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @GetMapping
    public ResponseEntity findAll(Pageable page){
        Page<Product> products = productService.listAll(page);
        return ResponseEntity.ok().body(products);
    }

    @GetMapping("/{name}")
    public ResponseEntity findByName(@PathVariable String name) throws ProductNotFoundException {
        Product product = productService.findByName(name);
        return ResponseEntity.ok().body(product);
    }

}
