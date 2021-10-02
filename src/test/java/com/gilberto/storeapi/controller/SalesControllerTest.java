package com.gilberto.storeapi.controller;

import com.gilberto.storeapi.entity.Product;
import com.gilberto.storeapi.exception.ProductNotFoundException;
import com.gilberto.storeapi.service.SalesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SalesControllerTest {

    private static final String STORE_API_URL_PATH = "/sales";
    private static final String STORE_API_SUBPATH_CLEAN_URL = "/clean";

    private MockMvc mockMvc;

    @Mock
    private SalesService salesService;

    @InjectMocks
    private SalesController salesController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salesController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);
        int quantity = 2;

        //when
        when(salesService.sales(quantity,expectedProduct.getName())).thenReturn(new String());


        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH + "?n=" + quantity + "&name=" + expectedProduct.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenGETIsCalledWithoutRegisteredNameThenNotFoundStatusIsReturned() throws Exception {
        // given
        Product expectedProduct = new Product(1L,"cerveja", 1, 31.90, 40.00);
        int quantity = 2;

        //when
        when(salesService.sales(quantity,expectedProduct.getName())).thenThrow(ProductNotFoundException.class);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH + "?n=" + quantity + "&name=" + expectedProduct.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGETIsCalledToCleanTheSalesCalculationsThenOkStatusIsReturned() throws Exception {

        //when
        doNothing().when(salesService).cleanItemList();

        // then
        mockMvc.perform(MockMvcRequestBuilders.get(STORE_API_URL_PATH + STORE_API_SUBPATH_CLEAN_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
