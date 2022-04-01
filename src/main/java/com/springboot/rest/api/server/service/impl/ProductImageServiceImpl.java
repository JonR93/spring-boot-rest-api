package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Product;
import com.springboot.rest.api.server.entity.ProductImage;
import com.springboot.rest.api.server.exception.MyAPIException;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.ProductImageDto;
import com.springboot.rest.api.server.repository.ProductImageRepository;
import com.springboot.rest.api.server.repository.ProductRepository;
import com.springboot.rest.api.server.service.ProductImageService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private ProductRepository productRepository;
    private ProductImageRepository productImageRepository;

    @Override
    public ProductImageDto addImageToProduct(Long productId, ProductImageDto productImageDto) {
        ProductImage productImage = ObjectMapperUtil.map(productImageDto, ProductImage.class);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","id",productId));
        productImage.setProduct(product);
        ProductImage savedProductImage = productImageRepository.save(productImage);
        return ObjectMapperUtil.map(savedProductImage, ProductImageDto.class);
    }

    @Override
    public List<ProductImageDto> getImagesForProduct(Long productId) {
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        return ObjectMapperUtil.mapAll(productImages,ProductImageDto.class);
    }

    @Override
    public ProductImageDto getImage(Long productId, Long productImageId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","id",productId));
        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(() -> new ResourceNotFoundException("ProductImage","id",productImageId));
        if(productImage.getProduct().getId() != product.getId()){
            throw new MyAPIException(HttpStatus.BAD_REQUEST, "Image does not belong to product.");
        }
        return ObjectMapperUtil.map(productImage,ProductImageDto.class);
    }

    @Override
    public ProductImageDto updateImage(Long productId, Long productImageId, ProductImageDto productImageDto) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","id",productId));
        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(() -> new ResourceNotFoundException("ProductImage","id",productImageId));
        if(productImage.getProduct().getId() != product.getId()){
            throw new MyAPIException(HttpStatus.BAD_REQUEST, "Image does not belong to product.");
        }

        productImage.setPrimary(productImageDto.isPrimary());
        productImage.setAffiliateUrl(productImageDto.getAffiliateUrl());
        productImage.setSequence(productImageDto.getSequence());

        ProductImage updatedProductImage = productImageRepository.save(productImage);

        return ObjectMapperUtil.map(updatedProductImage,ProductImageDto.class);
    }

    @Override
    public void deleteImage(Long productId, Long productImageId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product","id",productId));
        ProductImage productImage = productImageRepository.findById(productImageId).orElseThrow(() -> new ResourceNotFoundException("ProductImage","id",productImageId));
        if(productImage.getProduct().getId() != product.getId()){
            throw new MyAPIException(HttpStatus.BAD_REQUEST, "Image does not belong to product.");
        }
        productImageRepository.delete(productImage);
    }
}
