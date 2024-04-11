package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String > {
}
