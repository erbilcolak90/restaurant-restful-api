package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.orderproduct.CreateOrderProductInput;
import com.example.restaurantrestful.dto.inputs.orderproduct.DeleteOrderProductInput;
import com.example.restaurantrestful.dto.inputs.orderproduct.UpdateOrderProductQuantityInput;
import com.example.restaurantrestful.dto.payloads.OrderProductPayload;
import com.example.restaurantrestful.entity.OrderProduct;
import com.example.restaurantrestful.entity.Product;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderProductServiceTest {

    @InjectMocks
    private OrderProductService orderProductServiceMock;

    @Mock
    private OrderProductRepository orderProductRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    private OrderProduct orderProductMock;

    private Product productMock;

    private OrderProductPayload orderProductPayloadMock;

    @BeforeEach
    void setUp(){
        orderProductMock = new OrderProduct();
        orderProductMock.setId("test_id");
        orderProductMock.setOrderId("test_order_id");
        orderProductMock.setProductId("test_product_id");
        orderProductMock.setQuantity(3);
        orderProductMock.setPrice(100.0);

        productMock = new Product();
        productMock.setId("test_product_id");
        productMock.setName("test_product_name");
        productMock.setPrice(100);
        productMock.setThumbnailId("test_thumbnail_id");
        productMock.setFoodId("test_food_id");

        orderProductPayloadMock = new OrderProductPayload("test_id","test_order_id","test_product_id",100.0);

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

    @DisplayName("getOrderProductByProductId should return valid orderProductPayload when given productId is exist")
    @Test
    void testGetOrderProductByProductId_success(){
        String test_product_id = "test_product_id";

        when(orderProductRepositoryMock.findByProductId(test_product_id)).thenReturn(Optional.ofNullable(orderProductMock));

        OrderProductPayload result = orderProductServiceMock.getOrderProductByProductId(test_product_id);

        assertNotNull(result);
    }

    @DisplayName("getOrderProductByProductId should throw custom exception orderProductNotFound when given productId does not exist")
    @Test
    void testGetOrderProductByProductId_orderProductNotFound(){
        String test_product_id = "test_product_id";

        when(orderProductRepositoryMock.findByProductId(test_product_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()-> orderProductServiceMock.getOrderProductByProductId(test_product_id));
    }

    @DisplayName("getOrderProductsByOrderId should return list orderProductPayload when given with orderId")
    @Test
    void testGetOrderProductsByOrderId_success(){
        String order_id = "test_order_id";

        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(orderProductMock);

        when(orderProductRepositoryMock.findByOrderId(order_id)).thenReturn(orderProductList);

        List<OrderProductPayload> result = orderProductServiceMock.getOrderProductsByOrderId(order_id);

        assertEquals(1,result.size());
    }

    @DisplayName("createOrderProduct should return valid orderProductPayload when given productName is exist")
    @Test
    void testCreateOrderProduct_success(){
        CreateOrderProductInput createOrderProductInput = new CreateOrderProductInput("test_order_id","test_product_name",1);

        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId("test_id");
        orderProduct.setOrderId(createOrderProductInput.getOrderId());
        orderProduct.setProductId(productMock.getId());
        orderProduct.setQuantity(createOrderProductInput.getQuantity());
        orderProduct.setPrice(createOrderProductInput.getQuantity() * productMock.getPrice());

        OrderProductPayload orderProductPayload = new OrderProductPayload(orderProduct.getId(), orderProduct.getOrderId(), orderProduct.getProductId(), orderProduct.getPrice());

        when(productServiceMock.getProductByName(createOrderProductInput.getProductName())).thenReturn(productMock);
        when(orderProductRepositoryMock.save(any(OrderProduct.class))).thenReturn(orderProduct);

        OrderProductPayload result = orderProductServiceMock.createOrderProduct(createOrderProductInput);

        assertNotNull(result);
        assertEquals(orderProductPayload, result);
    }

    @DisplayName("updateOrderProductQuantity should return valid orderProductPayload when given productName is exist in UpdateOrderProductQuantityInput ")
    @Test
    void testUpdateOrderProductQuantity_success(){
        UpdateOrderProductQuantityInput updateOrderProductQuantityInput = new UpdateOrderProductQuantityInput("test_order_id","test_product_name",3);
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductList.add(orderProductMock);

        when(productServiceMock.getProductByName(updateOrderProductQuantityInput.getProductName().toLowerCase())).thenReturn(productMock);
        when(orderProductRepositoryMock.findByOrderId(updateOrderProductQuantityInput.getOrderId())).thenReturn(orderProductList);

        OrderProductPayload result = orderProductServiceMock.updateOrderProductQuantity(updateOrderProductQuantityInput);

        assertNotNull(result);
    }

    @DisplayName("deleteOrderProduct should return true when given productName is exist and given quantity less than dbOrderProduct quantity from DeleteOrderProductInput")
    @Test
    void testDeleteOrderProduct_success(){
        DeleteOrderProductInput deleteOrderProductInput = new DeleteOrderProductInput("test_order_id","test_product_name",3);
        List<OrderProduct> orderProductList = new ArrayList<>();

        orderProductList.add(orderProductMock);
        when(productServiceMock.getProductByName(deleteOrderProductInput.getProductName().toLowerCase())).thenReturn(productMock);
        when(orderProductRepositoryMock.findByOrderId(deleteOrderProductInput.getOrderId())).thenReturn(orderProductList);
        when(orderProductRepositoryMock.save(orderProductMock)).thenReturn(orderProductMock);

        assertTrue(orderProductServiceMock.deleteOrderProduct(deleteOrderProductInput));
    }

    @DisplayName("deleteOrderProduct should return false when given productName exist but orderProduct not contains at orderProductList from deleteOrderProductInput")
    @Test
    void testDeleteOrderProduct_orderProductListNotContains_false(){
        DeleteOrderProductInput deleteOrderProductInput = new DeleteOrderProductInput("test_order_id","test_product_name",2);
        List<OrderProduct> orderProductList = new ArrayList<>();

        when(productServiceMock.getProductByName(deleteOrderProductInput.getProductName().toLowerCase())).thenReturn(productMock);
        when(orderProductRepositoryMock.findByOrderId(deleteOrderProductInput.getOrderId())).thenReturn(orderProductList);

        assertFalse(orderProductServiceMock.deleteOrderProduct(deleteOrderProductInput));
    }

    @DisplayName("deleteOrderProduct should throw custom exception orderProductQuantityLimitException when given productName exist orderProductList contains orderProduct but orderProduct quantity less than deleteOrderProductInput quantity from deleteOrderProductInput")
    @Test
    void testDeleteOrderProduct_orderProductQuantityLimitException(){
        DeleteOrderProductInput deleteOrderProductInput = new DeleteOrderProductInput("test_order_id","test_product_name",2);
        List<OrderProduct> orderProductList = new ArrayList<>();
        orderProductMock.setQuantity(1);
        orderProductList.add(orderProductMock);

        when(productServiceMock.getProductByName(deleteOrderProductInput.getProductName().toLowerCase())).thenReturn(productMock);
        when(orderProductRepositoryMock.findByOrderId(deleteOrderProductInput.getOrderId())).thenReturn(orderProductList);

        CustomException exception = assertThrows(CustomException.class, ()-> orderProductServiceMock.deleteOrderProduct(deleteOrderProductInput));

        assertEquals("Product quantity : "+orderProductMock.getQuantity() + " the number of products that want to be delete : " + deleteOrderProductInput.getQuantity(),exception.getMessage());
    }


    @AfterEach
    void tearDown() {

    }
}
