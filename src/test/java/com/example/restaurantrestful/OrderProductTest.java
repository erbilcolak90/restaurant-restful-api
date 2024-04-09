package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.payloads.OrderProductPayload;
import com.example.restaurantrestful.entity.OrderProduct;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.OrderProductRepository;
import com.example.restaurantrestful.service.OrderProductService;
import com.example.restaurantrestful.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderProductTest {

    @InjectMocks
    private OrderProductService orderProductServiceMock;

    @Mock
    private OrderProductRepository orderProductRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    private OrderProduct orderProductMock;

    @BeforeEach
    void setUp(){
        orderProductMock = new OrderProduct();
        orderProductMock.setId("test_id");
        orderProductMock.setOrderId("test_order_id");
        orderProductMock.setProductId("test_product_id");
        orderProductMock.setPrice(100.0);

    }

    @DisplayName("getOrderProductById should return valid orderProductPayload when given id is exist")
    @Test
    void testGetOrderProductById_success(){
        String test_id = "test_id";

        when(orderProductRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(orderProductMock));

        OrderProductPayload result = orderProductServiceMock.getOrderProductById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getOrderProductById should throw custom exception orderProductNotFound when given id does not exist")
    @Test
    void testGetOrderProductById_orderProductNotFound(){
        String test_id = "test_id";

        when(orderProductRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()-> orderProductServiceMock.getOrderProductById(test_id));
    }


    @AfterEach
    void tearDown() {

    }
}
