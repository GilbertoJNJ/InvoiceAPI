package com.gilberto.storeapi.controller;

import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.service.SalesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales")
@AllArgsConstructor
public class SalesController {

    private SalesService salesService;

    @GetMapping
    public ResponseEntity sales(@RequestParam(name = "n") Integer quantity,
                                @RequestParam(name = "name") String name) throws ProductNotFoundException {

        String items = salesService.sales(quantity,name);
        return ResponseEntity.ok().body(items);
    }

    @GetMapping("/clean")
    public ResponseEntity clean(){
        salesService.cleanItemList();
        return ResponseEntity.ok().body("The list of items was cleaned");
    }

}
