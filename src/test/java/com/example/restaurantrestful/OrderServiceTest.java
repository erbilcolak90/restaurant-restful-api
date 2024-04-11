package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Order;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.OrderRepository;
import com.example.restaurantrestful.service.OrderProductService;
import com.example.restaurantrestful.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderServiceMock;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private OrderProductService orderProductServiceMock;

    private Order orderMock;

    @BeforeEach
    void setUp(){
        orderMock = new Order();
        orderMock.setId("test_id");
        orderMock.setOrderIds(new ArrayList<>());
        orderMock.setTotalPrice(100.0);
        orderMock.setCompleted(false);

    }
    @DisplayName("getOrderById should return valid order when given id is exist")
    @Test
    void testGetOrderById_success(){
        String test_id = "test_id";

        when(orderRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(orderMock));

        Order result = orderServiceMock.getOrderById(test_id);

        assertNotNull(result);

    }

    @DisplayName("getOrderById should throw custom exception orderNotFound when given id does not exist")
    @Test
    void testGetOrderById_orderNotFound(){
        assertThrows(CustomException.class,()-> orderServiceMock.getOrderById(anyString()));
    }

    @AfterEach
    void tearDown(){

    }
}
