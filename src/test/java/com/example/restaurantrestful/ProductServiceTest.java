package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.enums.ProductStatusEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.ProductRepository;
import com.example.restaurantrestful.service.FoodService;
import com.example.restaurantrestful.service.ProductService;
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
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private FoodService foodServiceMock;

    private Product productMock;

    @BeforeEach
    void setUp(){
        productMock = new Product();
        productMock.setId("test_id");
        productMock.setName("test_name");
        productMock.setFoodId("test_food_id");
        productMock.setThumbnailId("test_thumbnail_id");
        productMock.setPrice(100.0);
        productMock.setStatus(ProductStatusEnums.WAITING);

    }

    @DisplayName("getProductById should return valid product when given id is exist")
    @Test
    void testGetProductById_success(){
        String test_id = "test_id";

        when(productRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.getProductById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getProductById should throw custom exception productNotFound when given id does not exist")
    @Test
    void testGetProductById_productNotFound(){
        String test_id = "test_id";

        when(productRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()->productService.getProductById(test_id));

    }

    @DisplayName("getProductByName should return valid product when given name is exist")
    @Test
    void testGetProductByName_success(){
        String test_name = "test_name";

        when(productRepositoryMock.findByName(test_name)).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.getProductById(test_name);

        assertNotNull(result);
    }

    @DisplayName("getProductByName should throw custom exception productNotFound when given name does not exist")
    @Test
    void testGetProductByName_productNotFound(){
        String test_name = "test_name";

        when(productRepositoryMock.findByName(test_name)).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()->productService.getProductByName(test_name));

    }
}
