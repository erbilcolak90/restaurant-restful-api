package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.orderproduct.CreateOrderProductInput;
import com.example.restaurantrestful.dto.inputs.orderproduct.UpdateOrderProductQuantityInput;
import com.example.restaurantrestful.dto.payloads.OrderProductPayload;
import com.example.restaurantrestful.entity.OrderProduct;
import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.enums.ProductStatusEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.OrderProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;

    public OrderProductService(OrderProductRepository orderProductRepository, ProductService productService) {
        this.orderProductRepository = orderProductRepository;
        this.productService = productService;
    }

    public OrderProductPayload getOrderProductById(String id) {
        OrderProduct dbOrderProduct = orderProductRepository.findById(id).orElseThrow(CustomException::orderProductNotFound);
        return OrderProductPayload.convert(dbOrderProduct);
    }

    public OrderProductPayload getOrderProductByProductId(String productId) {
        OrderProduct dbOrderProduct = orderProductRepository.findByProductId(productId).orElseThrow(CustomException::orderProductNotFound);
        return OrderProductPayload.convert(dbOrderProduct);
    }

    public List<OrderProductPayload> getOrderProductsByOrderId(String orderId) {
        List<OrderProduct> orderProductList = orderProductRepository.findByOrderId(orderId);
        List<OrderProductPayload> orderProductPayloadList = orderProductList.stream().map(orderProduct -> OrderProductPayload.convert(orderProduct)).toList();
        return orderProductPayloadList;
    }

    @Transactional
    public OrderProductPayload createOrderProduct(CreateOrderProductInput createOrderProductInput) {
        Product dbProduct = productService.getProductByName(createOrderProductInput.getProductName());
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(createOrderProductInput.getOrderId());
        orderProduct.setQuantity(createOrderProductInput.getQuantity());
        orderProduct.setProductId(dbProduct.getId());
        orderProduct.setPrice(createOrderProductInput.getQuantity() * dbProduct.getPrice());

        OrderProduct dbOrderProduct = orderProductRepository.save(orderProduct);

        return OrderProductPayload.convert(dbOrderProduct);
    }

    @Transactional
    public OrderProductPayload updateOrderProductQuantity(UpdateOrderProductQuantityInput updateOrderProductQuantityInput){

        Product dbProduct = productService.getProductByName(updateOrderProductQuantityInput.getProductName().toLowerCase());

        List<OrderProduct> dbOrderProductList = orderProductRepository.findByOrderId(updateOrderProductQuantityInput.getOrderId());

        OrderProduct updatedOrderProduct = new OrderProduct();
        for(OrderProduct item: dbOrderProductList){
            if(item.getProductId().equals(dbProduct.getId())){
                item.setQuantity(updateOrderProductQuantityInput.getQuantity());
                item.setUpdateDate(new Date());
                orderProductRepository.save(item);
                updatedOrderProduct = item;
                break;
            }
        }
        return OrderProductPayload.convert(updatedOrderProduct);
    }
}
