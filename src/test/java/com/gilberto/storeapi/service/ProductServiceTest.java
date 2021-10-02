package com.gilberto.storeapi.service;

import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void whenListProductsIsCalledThenReturnAListOfProducts(){
        //given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        PageRequest pageRequest = PageRequest.of(0,20, Sort.unsorted());

        //when
        when(productRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(asList(expectedProduct)));

        //then
        Page<Product> products = productService.listAll(pageRequest);

        assertThat(products, is(not(empty())));
        assertThat(products.stream().findFirst(), is(equalTo(Optional.of(expectedProduct))));

    }

    @Test
    void whenValidProductNameIsGivenThenReturnAProduct() throws ProductNotFoundException {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        // when
        when(productRepository.findByName(expectedProduct.getName())).thenReturn(Optional.of(expectedProduct));

        // then
        Product product = productService.findByName(expectedProduct.getName());

        assertThat(product, is(equalTo(expectedProduct)));
    }

    @Test
    void whenNotRegisteredProductNameIsGivenThenThrowAnException() {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        // when
        when(productRepository.findByName(expectedProduct.getName())).thenReturn(Optional.empty());

        // then
        assertThrows(ProductNotFoundException.class, () -> productService.findByName(expectedProduct.getName()));
    }

}
