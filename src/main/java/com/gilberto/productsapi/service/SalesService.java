package com.gilberto.productsapi.service;

import com.gilberto.productsapi.entity.Product;
import com.gilberto.productsapi.exception.ProductNotFoundException;
import com.gilberto.productsapi.repository.ProductRepository;
import com.gilberto.productsapi.utils.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SalesService {

    private ProductRepository productRepository;

    public Item newItem(Product product, Integer quantity){
        Item item = new Item();
        item.setName(product.getName());
        item.setQuantity(quantity);
        item.setSellPrice(product.getSellPrice());
        return item;
    }

    public Product findByName(String name) throws ProductNotFoundException {
        Product product = verifyIfExists(name);
        return product;
    }
    private Product verifyIfExists(String name) throws ProductNotFoundException {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(name));
    }
}
