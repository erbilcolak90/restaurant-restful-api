package com.example.restaurantrestful.service;

import com.example.restaurantrestful.repository.jpa.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderProductService orderProductService;

    public OrderService(OrderRepository orderRepository, OrderProductService orderProductService) {
        this.orderRepository = orderRepository;
        this.orderProductService = orderProductService;
    }
}
