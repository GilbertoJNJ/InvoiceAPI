package com.gilberto.storeapi.service;

import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.repository.ProductRepository;
import com.gilberto.storeapi.entity.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SalesService {

    private ProductRepository productRepository;
    private List<Item> items;


    public String sales(Integer quantity, String name) throws ProductNotFoundException {
        items = new ArrayList<>();
        Product product = verifyIfExists(name);
        Item item = newItem(product, quantity);
        items.add(item);

        Double total = 0.0;
        for (Item value : items) {
            Integer quant = value.getQuantity();
            Double sellPrice = value.getSellPrice();
            Double parcel = quant * sellPrice;
            total += parcel;
        }

        return items+"\nTotal = " + total;
    }

    public void cleanItemList(){
        items.clear();
    }

    public Boolean verifyIfIsEmpty() {
        if(items.isEmpty()){
            return true;
        }
        return false;
    }

    private Product verifyIfExists(String name) throws ProductNotFoundException {
        return productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException(name));
    }

    private Item newItem(Product product, Integer quantity){
        Item item = new Item();
        item.setName(product.getName());
        item.setQuantity(quantity);
        item.setSellPrice(product.getSellPrice());
        return item;
    }

}
