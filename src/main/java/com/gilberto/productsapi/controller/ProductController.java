package com.gilberto.productsapi.controller;

import com.gilberto.productsapi.entity.Product;
import com.gilberto.productsapi.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductRepository productRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Product> listAll(Pageable page){
        return productRepository.findAll(page);
    }

    @GetMapping("/{name}")
    public ResponseEntity listByName(@PathVariable String name){
        Optional<Product> product = productRepository.findByName(name);
        if(product.isPresent()){
            return ResponseEntity.ok().body(product.get());
        }
        return ResponseEntity.notFound().build();
    }
/*
    @GetMapping("/{id}")
    public ResponseEntity listById(@PathVariable Long id){
        Optional<Product> product = productRepository.findById(id);
        if(product.isPresent()){
            return ResponseEntity.ok().body(product.get());
        }
        return ResponseEntity.notFound().build();
    }
    */

}
