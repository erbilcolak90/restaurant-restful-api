package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.product.*;
import com.example.restaurantrestful.entity.Food;
import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.enums.ProductStatusEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.ProductRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final FoodService foodService;

    public ProductService(ProductRepository productRepository, FoodService foodService) {
        this.productRepository = productRepository;
        this.foodService = foodService;
    }

    public Product getProductById(String id) {
        return productRepository.findById(id).orElseThrow(CustomException::productNotFound);
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name.toLowerCase()).orElseThrow(CustomException::productNotFound);
    }

    public List<Product> getAllProductsByStatus(ProductStatusEnums statusEnum) {
        return productRepository.findAllByStatusOrderByUpdateDateDesc(statusEnum);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Product createProduct(CreateProductInput createProductInput) {
        Food dbFood = foodService.getFoodByName(createProductInput.getName());

        Product newProduct = new Product();
        newProduct.setName(createProductInput.getName());
        newProduct.setPrice(createProductInput.getPrice());
        newProduct.setThumbnailId(null);

        try {
            makeFoodAsync(dbFood.getName());
            newProduct.setStatus(ProductStatusEnums.READY);

        } catch (Exception e) {
            e.getMessage();
        }

        newProduct.setFoodId(dbFood.getId());
        productRepository.save(newProduct);
        return newProduct;
    }

    @Transactional
    public Product updateProductPrice(UpdateProductPriceInput updateProductPriceInput) {
        Product dbProduct = productRepository.findById(updateProductPriceInput.getId()).orElseThrow(CustomException::productNotFound);

        if (updateProductPriceInput.getNewPrice() > 0.0) {
            dbProduct.setPrice(updateProductPriceInput.getNewPrice());
            dbProduct.setUpdateDate(new Date());
            productRepository.save(dbProduct);

            return dbProduct;
        } else {
            throw CustomException.productPriceLimitException();
        }

    }

    @Transactional
    public Product updateProductStatus(UpdateProductStatusInput updateProductStatusInput) {
        Product dbProduct = productRepository.findById(updateProductStatusInput.getId()).orElseThrow(CustomException::productNotFound);

        if (!dbProduct.getStatus().equals(updateProductStatusInput.getStatus())) {
            dbProduct.setStatus(updateProductStatusInput.getStatus());
            dbProduct.setUpdateDate(new Date());
            productRepository.save(dbProduct);

            return dbProduct;
        } else {
            throw CustomException.productStatusIsSameWithInput();
        }
    }

    @Transactional
    public Product updateProductThumbnail(UpdateProductThumbnailInput updateProductThumbnailInput) {
        Product dbProduct = productRepository.findById(updateProductThumbnailInput.getId()).orElseThrow(CustomException::productNotFound);

        if (!dbProduct.getThumbnailId().equals(updateProductThumbnailInput.getThumbnailId())) {
            dbProduct.setThumbnailId(updateProductThumbnailInput.getThumbnailId());
            dbProduct.setUpdateDate(new Date());
            productRepository.save(dbProduct);

            return dbProduct;
        } else {
            throw CustomException.productThumbnailIdSameWithInput();
        }

    }

    @Transactional
    public Product updateProductName(UpdateProductNameInput updateProductNameInput) {
        Product dbProduct = productRepository.findById(updateProductNameInput.getId()).orElseThrow(CustomException::productNotFound);

        Food dbFood = foodService.getFoodByName(updateProductNameInput.getName().toLowerCase());

        dbProduct.setName(dbFood.getName());
        dbProduct.setUpdateDate(new Date());
        productRepository.save(dbProduct);

        return dbProduct;
    }

    @Async
    public void makeFoodAsync(String foodName) {
        foodService.makeFood(foodName);
    }
}
