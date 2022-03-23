package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.payload.ProductDto;
import com.springboot.rest.api.server.payload.ProductsDto;

public interface ProductService {
    ProductDto createProduct(ProductDto categoryDto);
    ProductDto getProductById(long id);
    ProductDto getProductByName(String name);
    ProductsDto findProducts(int pageNo, int pageSize, String sortBy, String sortDir);
    ProductDto updateProduct(ProductDto categoryDto, long id);
    void deleteProduct(long id);
}
