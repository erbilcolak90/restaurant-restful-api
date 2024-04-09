package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {

    Optional<OrderProduct> findByProductId(String productId);
}
