package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {
}
