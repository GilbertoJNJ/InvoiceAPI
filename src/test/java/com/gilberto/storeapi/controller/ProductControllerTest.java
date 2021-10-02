package com.gilberto.storeapi.controller;

import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    private static final String STORE_API_URL_PATH = "/products";

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenGETListWithProductsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        PageRequest pageRequest = PageRequest.of(0,20, Sort.unsorted());

        //when
        when(productService.listAll(pageRequest)).thenReturn(new PageImpl<>(asList(expectedProduct)));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].name", is(expectedProduct.getName())))
                .andExpect(jsonPath("$.content.[0].buyPrice", is(expectedProduct.getBuyPrice())))
                .andExpect(jsonPath("$.content.[0].sellPrice", is(expectedProduct.getSellPrice())));
    }

    @Test
    void whenGETListWithoutProductsIsCalledThenOkStatusIsReturned() throws Exception {
        // given
        PageRequest pageRequest = PageRequest.of(0,20, Sort.unsorted());

        //when
        when(productService.listAll(pageRequest)).thenReturn(new PageImpl<>(Collections.EMPTY_LIST));

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        //when
        when(productService.findByName(expectedProduct.getName())).thenReturn(expectedProduct);


        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH + "/" + expectedProduct.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(expectedProduct.getName())))
                .andExpect(jsonPath("$.buyPrice", is(expectedProduct.getBuyPrice())))
                .andExpect(jsonPath("$.sellPrice", is(expectedProduct.getSellPrice())));
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);

        //when
        when(productService.findByName(expectedProduct.getName())).thenThrow(ProductNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH + "/" + expectedProduct.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
