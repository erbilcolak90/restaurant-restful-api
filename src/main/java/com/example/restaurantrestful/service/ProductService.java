package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final FoodService foodService;

    public ProductService(ProductRepository productRepository, FoodService foodService) {
        this.productRepository = productRepository;
        this.foodService = foodService;
    }

    public Product getProductById(String id){
        return productRepository.findById(id).orElseThrow(CustomException::productNotFound);
    }
}
