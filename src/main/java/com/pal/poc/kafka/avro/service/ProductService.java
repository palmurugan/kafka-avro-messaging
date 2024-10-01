package com.pal.poc.kafka.avro.service;

import com.pal.poc.kafka.avro.messaging.dto.Product;

import java.util.Optional;

public interface ProductService {

    Optional<Product> createProduct(Product product);
}
