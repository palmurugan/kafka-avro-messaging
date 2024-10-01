package com.pal.poc.kafka.avro.controller;

import com.pal.poc.kafka.avro.messaging.dto.Product;
import com.pal.poc.kafka.avro.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Create a product
     *
     * @param product the product to create
     * @return the created product
     */
    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product) {
        log.info("Rest request to create product='{}'", product);
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created");
    }
}
