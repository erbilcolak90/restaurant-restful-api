package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.order.CreateOrderInput;
import com.example.restaurantrestful.dto.inputs.order.GetAllOrdersByDateRangeInput;
import com.example.restaurantrestful.dto.inputs.order.GetAllOrdersInput;
import com.example.restaurantrestful.dto.inputs.orderproduct.CreateOrderProductInput;
import com.example.restaurantrestful.dto.payloads.OrderProductPayload;
import com.example.restaurantrestful.entity.Order;
import com.example.restaurantrestful.enums.SortBy;
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
import org.springframework.data.domain.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    void setUp() {
        orderMock = new Order();
        orderMock.setId("test_id");
        orderMock.setOrderProductIds(new ArrayList<>());
        orderMock.setTotalPrice(100.0);
        orderMock.setCompleted(false);

    }

    @DisplayName("getOrderById should return valid order when given id is exist")
    @Test
    void testGetOrderById_success() {
        String test_id = "test_id";

        when(orderRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(orderMock));

        Order result = orderServiceMock.getOrderById(test_id);

        assertNotNull(result);

    }

    @DisplayName("getOrderById should throw custom exception orderNotFound when given id does not exist")
    @Test
    void testGetOrderById_orderNotFound() {
        assertThrows(CustomException.class, () -> orderServiceMock.getOrderById(anyString()));
    }

    @DisplayName("getAllOrders should return page order when given getAllOrderInput is exist")
    @Test
    void testGetAllOrders_success() {
        GetAllOrdersInput getAllOrdersInput = new GetAllOrdersInput(1, 1, SortBy.ASC, "id");

        List<Order> orderList = new ArrayList<>();
        orderList.add(orderMock);
        orderList.add(new Order("test_order_id_2", new ArrayList<>(), 250.0, false));
        orderList.add(new Order("test_order_id_3", new ArrayList<>(), 450.0, false));

        Pageable pageable = PageRequest.of(getAllOrdersInput.getPageNumber(), getAllOrdersInput.getPageSize(), Sort.by(Sort.Direction.valueOf(getAllOrdersInput.getSortBy().toString()), getAllOrdersInput.getFieldName()));

        Page<Order> orderPage = new PageImpl<>(orderList, pageable, orderList.size());

        when(orderRepositoryMock.findAll(pageable)).thenReturn(orderPage);

        Page<Order> result = orderServiceMock.getAllOrders(getAllOrdersInput);

        assertNotNull(result);
    }

    @DisplayName("getAllOrdersByIsCompletedFalse should return list order")
    @Test
    void testGetAllOrdersByIsCompletedFalse_success() {
        List<Order> orderList = new ArrayList<>();
        orderList.add(orderMock);
        orderList.add(new Order("test_order_id_5", new ArrayList<>(), 450.0, false));
        orderList.add(new Order("test_order_id_6", new ArrayList<>(), 450.0, false));
        orderList.add(new Order("test_order_id_7", new ArrayList<>(), 450.0, false));
        orderList.add(new Order("test_order_id_8", new ArrayList<>(), 450.0, false));

        when(orderRepositoryMock.findByIsCompletedFalse()).thenReturn(orderList);

        List<Order> result = orderServiceMock.getAllOrdersByIsCompletedFalse();

        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @DisplayName("getAllOrdersByDateRange should return list order when given getAllOrdersByDateRangeInput")
    @Test
    void testGetAllOrdersByDateRange_success(){
        LocalDate startDate = LocalDate.of(2024,03,15);
        LocalDate endDate = LocalDate.of(2024,04,25);
        LocalDate orderMockDate= LocalDate.of(2024,3,14);
        GetAllOrdersByDateRangeInput getAllOrdersByDateRangeInput = new GetAllOrdersByDateRangeInput(startDate,endDate);

        List<Order> orderList = new ArrayList<>();
        orderMock.setCreateDate(Date.from(orderMockDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        orderList.add(new Order("test_order_id_5", new ArrayList<>(), 450.0, false));
        orderList.add(new Order("test_order_id_6", new ArrayList<>(), 450.0, false));
        orderList.add(new Order("test_order_id_7", new ArrayList<>(), 450.0, false));
        orderList.add(new Order("test_order_id_8", new ArrayList<>(), 450.0, false));

        when(orderRepositoryMock.findByCreateDateBetween(startDate,endDate)).thenReturn(orderList);

        List<Order> result = orderServiceMock.getAllOrdersByDateRange(getAllOrdersByDateRangeInput);

        assertEquals(4,result.size());
    }

    @DisplayName("createOrder should return valid order when given productNames exist in createOrderInput")
    @Test
    void testCreateOrder_success(){
        HashMap<String, Integer> productNames = new HashMap<>();
        productNames.put("test_product_name_1",2);
        productNames.put("test_product_name_2",3);
        productNames.put("test_product_name_3",4);

        CreateOrderInput createOrderInput = new CreateOrderInput(productNames);

        CreateOrderProductInput createOrderProductInput = new CreateOrderProductInput("test_id", "test_product_name_1",2);
        CreateOrderProductInput createOrderProductInput2 = new CreateOrderProductInput("test_id", "test_product_name_2",3);
        CreateOrderProductInput createOrderProductInput3 = new CreateOrderProductInput("test_id", "test_product_name_3",4);

        OrderProductPayload orderProductPayload = new OrderProductPayload("test_orderProduct_id_1", "test_id", "test_product_id_1",100.0);
        OrderProductPayload orderProductPayload2 = new OrderProductPayload("test_orderProduct_id_2", "test_id", "test_product_id_2",100.0);
        OrderProductPayload orderProductPayload3 = new OrderProductPayload("test_orderProduct_id_3", "test_id", "test_product_id_3",100.0);


        when(orderRepositoryMock.save(new Order())).thenReturn(orderMock);
        when(orderProductServiceMock.createOrderProduct(createOrderProductInput)).thenReturn(orderProductPayload);
        when(orderProductServiceMock.createOrderProduct(createOrderProductInput2)).thenReturn(orderProductPayload2);
        when(orderProductServiceMock.createOrderProduct(createOrderProductInput3)).thenReturn(orderProductPayload3);

        Order result = orderServiceMock.createOrder(createOrderInput);

        assertNotNull(result);
        assertEquals(3,result.getOrderProductIds().size());

    }

    @AfterEach
    void tearDown() {

    }
}
