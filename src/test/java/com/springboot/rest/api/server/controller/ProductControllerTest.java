package com.springboot.rest.api.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.rest.api.server.payload.ProductDto;
import com.springboot.rest.api.server.payload.ProductsDto;
import com.springboot.rest.api.server.service.ProductService;
import com.springboot.rest.api.server.utils.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(useDefaultFilters = false)
@Import(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createProduct() throws Exception{
        ProductDto product = ProductDto.builder()
                .id(null)
                .name("something")
                .price(new BigDecimal("1.00"))
                .build();

        given(productService.createProduct(product)).willReturn(
                ProductDto.builder()
                .id(1L)
                .name("something")
                .price(new BigDecimal("1.00"))
                .build());

        ResultActions response = mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice().toString())));
    }

    @Test
    void getProductById() throws Exception{
        ProductDto product = ProductDto.builder()
                .id(1L)
                .name("something")
                .price(new BigDecimal("1.00"))
                .build();

        given(productService.getProductById(1L)).willReturn(product);

        ResultActions response = mockMvc.perform(get("/api/v1/products/{id}", 1L));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice().toString())));
    }

    @Test
    void getAllProducts() throws Exception{
        List<ProductDto> listOfProducts = new ArrayList<>();
        listOfProducts.add(ProductDto.builder()
                .id(1L)
                .name("something")
                .price(new BigDecimal("1.00"))
                .build());
        listOfProducts.add(ProductDto.builder()
                .id(2L)
                .name("something else")
                .price(new BigDecimal("2.00"))
                .build());
        listOfProducts.add(ProductDto.builder()
                .id(3L)
                .name("another thing")
                .price(new BigDecimal("3.00"))
                .build());

        ProductsDto products = ProductsDto.builder()
                .content(listOfProducts)
                .pageNo(0)
                .pageSize(listOfProducts.size())
                .totalElements(listOfProducts.size())
                .totalPages(1)
                .last(true)
                .build();

        given(productService.findProducts(0,10, AppConstants.DEFAULT_SORT_BY,AppConstants.DEFAULT_SORT_DIRECTION)).willReturn(products);

        ResultActions response = mockMvc.perform(get("/api/v1/products",
                AppConstants.DEFAULT_PAGE_NUMBER,
                AppConstants.DEFAULT_PAGE_SIZE,
                AppConstants.DEFAULT_SORT_BY,
                AppConstants.DEFAULT_SORT_DIRECTION));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content.size()",
                        is(listOfProducts.size())));
    }

    @Test
    void updateProduct() throws Exception{
        ProductDto updatedProduct = ProductDto.builder()
                .id(1L)
                .name("something")
                .price(new BigDecimal("1.00"))
                .build();

        given(productService.updateProduct(updatedProduct,1L)).willReturn(updatedProduct);

        ResultActions response = mockMvc.perform(put("/api/v1/products/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedProduct.getName())))
                .andExpect(jsonPath("$.price", is(updatedProduct.getPrice().toString())));
    }

    @Test
    void deleteProduct() throws Exception{
        willDoNothing().given(productService).deleteProduct(1L);
        ResultActions response = mockMvc.perform(delete("/api/v1/products/{id}", 1L));
        response.andExpect(status().isOk())
                .andDo(print());
    }
}