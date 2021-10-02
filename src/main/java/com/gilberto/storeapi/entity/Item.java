package com.gilberto.storeapi.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Item {

    private String name;
    private Integer quantity;
    private Double sellPrice;


    @Override
    public String toString() {

        return name + " => " + quantity + " x " + sellPrice + " = " + quantity*sellPrice;
    }
}
