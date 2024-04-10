package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.payloads.OrderProductPayload;
import com.example.restaurantrestful.entity.OrderProduct;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.OrderProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;

    public OrderProductService(OrderProductRepository orderProductRepository, ProductService productService) {
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
    }

    public OrderProductPayload getOrderProductById(String id){
        OrderProduct dbOrderProduct = orderProductRepository.findById(id).orElseThrow(CustomException::orderProductNotFound);
        return OrderProductPayload.convert(dbOrderProduct);
    }

    public OrderProductPayload getOrderProductByProductId(String productId){
        OrderProduct dbOrderProduct = orderProductRepository.findByProductId(productId).orElseThrow(CustomException::orderProductNotFound);
        return OrderProductPayload.convert(dbOrderProduct);
    }

    public List<OrderProductPayload> getOrderProductsByOrderId(String orderId){
        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId);
        List<OrderProductPayload> orderProductPayloadList = orderProductList.stream().map(orderProduct -> OrderProductPayload.convert(orderProduct)).toList();
        return orderProductPayloadList;
    }
}
