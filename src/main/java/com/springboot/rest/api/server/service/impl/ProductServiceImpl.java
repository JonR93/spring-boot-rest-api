package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Product;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.ProductsDto;
import com.springboot.rest.api.server.payload.ProductDto;
import com.springboot.rest.api.server.repository.ProductRepository;
import com.springboot.rest.api.server.service.ProductService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = ObjectMapperUtil.map(productDto,Product.class);
        Product newProduct = productRepository.save(product);
        return ObjectMapperUtil.map(newProduct,ProductDto.class);
    }

    @Override
    public ProductDto getProductById(long id) {
        Product product = findProductById(id);
        return ObjectMapperUtil.map(product,ProductDto.class);
    }

    @Override
    public ProductDto getProductByName(String name) {
        Product product = findProductByName(name);
        return ObjectMapperUtil.map(product,ProductDto.class);
    }

    @Override
    public ProductsDto findProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);

        return new ProductsDto(products);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, long id) {
        Product product = findProductById(id);
        product.setName(productDto.getName());
        Product updatedProduct = productRepository.save(product);
        return ObjectMapperUtil.map(updatedProduct,ProductDto.class);
    }

    @Override
    public void deleteProduct(long id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    private Product findProductById(long id){
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    private Product findProductByName(String name){
        return productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product", "name", name));
    }
}
