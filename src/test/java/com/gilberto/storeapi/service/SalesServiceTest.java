package com.gilberto.storeapi.service;

import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {

    private static final String INVALID_PRODUCT_NAME = "invalidName";

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private SalesService salesService;

    @Test
    void whenSalesCalculationIsCalledThenReturnAListOfItemsAndTotal() throws ProductNotFoundException {
        //given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        //when
        when(productRepository.findByName(expectedProduct.getName())).thenReturn(Optional.of(expectedProduct));

        //then
        int quantity = 2;
        String sales = salesService.sales(quantity, expectedProduct.getName());

        assertThat(sales, is(equalTo("[\ncerveja => 2 x 40.0 = 80.0]\nTotal = 80.0")));
    }

    @Test
    void whenSalesCalculationIsCalledWithInvalidNameThenThrowAnException(){

        //when
        when(productRepository.findByName(INVALID_PRODUCT_NAME)).thenReturn(Optional.empty());

        //then
        int quantity = 2;
        assertThrows(ProductNotFoundException.class, () -> salesService.sales(quantity, INVALID_PRODUCT_NAME));
    }

    @Test
    void whenCleanItemListIsCalledThenReturnAnEmptyList() throws ProductNotFoundException {
        //given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        //when
        when(productRepository.findByName(expectedProduct.getName())).thenReturn(Optional.of(expectedProduct));

        //then
        int quantity = 2;
        salesService.sales(quantity, expectedProduct.getName());
        assertThat(salesService.verifyIfIsEmpty(), is(equalTo(false)));

        //then
        salesService.cleanItemList();
        assertThat(salesService.verifyIfIsEmpty(), is(equalTo(true)));
    }

}
