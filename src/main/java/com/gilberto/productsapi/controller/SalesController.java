package com.gilberto.productsapi.controller;

import com.gilberto.productsapi.entity.Product;
import com.gilberto.productsapi.exception.ProductNotFoundException;
import com.gilberto.productsapi.service.SalesService;
import com.gilberto.productsapi.utils.Item;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
public class SalesController {

    private SalesService salesService;
    private List<Item> items;

    @GetMapping
    public ResponseEntity sales(@RequestParam(name = "n") Integer quantity,
                                @RequestParam(name = "name") String name) throws ProductNotFoundException {

        Product product = salesService.findByName(name);
        Item item = salesService.newItem(product, quantity);
        items.add(item);

        Double total = 0.0;
        for(int i=1; i<items.size();i++){
            Integer n = items.get(i).getQuantity();
            Double price = items.get(i).getSellPrice();
            Double parcel = n * price;
            total += parcel;
        }

        return ResponseEntity.ok().body(items+" "+"Total = "+total);
    }

}
