package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Order;
import com.example.restaurantrestful.repository.jpa.OrderRepository;
import com.example.restaurantrestful.service.OrderProductService;
import com.example.restaurantrestful.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderServiceMock;

    @Mock
    private OrderRepository orderRepository;

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

    @AfterEach
    void tearDown(){

    }
}
