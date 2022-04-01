package com.springboot.rest.api.server.controller;

import com.springboot.rest.api.server.payload.ProductImageDto;
import com.springboot.rest.api.server.payload.ProductsDto;
import com.springboot.rest.api.server.payload.ProductDto;
import com.springboot.rest.api.server.service.ProductImageService;
import com.springboot.rest.api.server.service.ProductService;
import com.springboot.rest.api.server.utils.AppConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "CRUD Rest endpoints for Product resources")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private ProductService productService;
    private ProductImageService productImageService;

    @ApiOperation(value = "Create Product")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto){
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get Product by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable(value = "id") Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @ApiOperation(value = "Get all Products")
    @GetMapping()
    public ProductsDto getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return productService.findProducts(pageNo, pageSize, sortBy, sortDir);
    }

    @ApiOperation(value = "Update Product by id")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @PathVariable(name = "id") long id){
        ProductDto updatedProduct = productService.updateProduct(productDto, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Product By id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "id") long id){
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted successfully.", HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Product Image By id")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "productId") long productId, @PathVariable(name = "imageId") long imageId){
        productImageService.deleteImage(productId,imageId);
        return new ResponseEntity<>("Product deleted successfully.", HttpStatus.OK);
    }
}
