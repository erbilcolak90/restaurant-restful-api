package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.order.*;
import com.example.restaurantrestful.dto.inputs.orderproduct.CreateOrderProductInput;
import com.example.restaurantrestful.dto.inputs.orderproduct.DeleteOrderProductInput;
import com.example.restaurantrestful.dto.payloads.OrderProductPayload;
import com.example.restaurantrestful.entity.Order;
import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderProductService orderProductService;

    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, OrderProductService orderProductService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.orderProductService = orderProductService;
        this.productService = productService;
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(CustomException::orderNotFound);
    }

    public Page<Order> getAllOrders(GetAllOrdersInput getAllOrdersInput) {

        Pageable pageable = PageRequest.of(getAllOrdersInput.getPageNumber(),
                getAllOrdersInput.getPageSize(),
                Sort.by(Sort.Direction.valueOf(getAllOrdersInput.getSortBy().toString()), getAllOrdersInput.getFieldName()));

        return orderRepository.findAll(pageable);
    }

    public List<Order> getAllOrdersByIsCompletedFalse() {
        return orderRepository.findByIsCompletedFalse();
    }

    public List<Order> getAllOrdersByDateRange(GetAllOrdersByDateRangeInput getAllOrdersByDateRangeInput) {

        return orderRepository.findByCreateDateBetween(getAllOrdersByDateRangeInput.getStartDate(), getAllOrdersByDateRangeInput.getEndDate());
    }

    @Transactional
    public Order createOrder(CreateOrderInput createOrderInput) {

        Order dbOrder = orderRepository.save(new Order());

        for (Map.Entry<String, Integer> productName : createOrderInput.getOrderProductNames().entrySet()) {
            CreateOrderProductInput createOrderProductInput = new CreateOrderProductInput(dbOrder.getId(), productName.getKey(), productName.getValue());
            OrderProductPayload orderProductPayload = orderProductService.createOrderProduct(createOrderProductInput);
            dbOrder.getOrderProductIds().add(orderProductPayload.getId());
            dbOrder.setTotalPrice(dbOrder.getTotalPrice() + orderProductPayload.getPrice());
        }

        orderRepository.save(dbOrder);
        return dbOrder;
    }

    @Transactional
    public Order addProductToOrder(AddProductToOrderInput addProductToOrderInput) {
        Order dbOrder = orderRepository.findById(addProductToOrderInput.getOrderId()).orElseThrow(CustomException::orderNotFound);
        CreateOrderProductInput createOrderProductInput = new CreateOrderProductInput(addProductToOrderInput.getOrderId(), addProductToOrderInput.getProductName(), addProductToOrderInput.getQuantity());
        OrderProductPayload orderProductPayload = orderProductService.createOrderProduct(createOrderProductInput);
        dbOrder.getOrderProductIds().add(orderProductPayload.getId());
        dbOrder.setTotalPrice(dbOrder.getTotalPrice()+ orderProductPayload.getPrice());

        dbOrder.setUpdateDate(new Date());
        orderRepository.save(dbOrder);

        return dbOrder;
    }

    @Transactional
    public Order deleteProductFromOrder(DeleteProductFromOrderInput deleteProductFromOrderInput){
        Order dbOrder = orderRepository.findById(deleteProductFromOrderInput.getOrderId()).orElseThrow(CustomException::orderNotFound);
        DeleteOrderProductInput deleteOrderProductInput = new DeleteOrderProductInput(deleteProductFromOrderInput.getOrderId(), deleteProductFromOrderInput.getProductName().toLowerCase(), deleteProductFromOrderInput.getQuantity());

        boolean result = orderProductService.deleteOrderProduct(deleteOrderProductInput);
        Product dbProduct = productService.getProductByName(deleteProductFromOrderInput.getProductName().toLowerCase());
        OrderProductPayload dbOrderProductPayload = orderProductService.getOrderProductByProductId(dbProduct.getId());

        if(result && dbOrderProductPayload.getPrice() / dbProduct.getPrice() == 1.0){
                dbOrder.getOrderProductIds().remove(dbOrderProductPayload.getId());
        }

        dbOrder.setUpdateDate(new Date());
        orderRepository.save(dbOrder);

        return dbOrder;
    }
}
