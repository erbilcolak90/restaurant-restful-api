package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.product.CreateProductInput;
import com.example.restaurantrestful.dto.inputs.product.UpdateProductPriceInput;
import com.example.restaurantrestful.entity.Food;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
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
    void setUp() {
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
    void testGetProductById_success() {
        String test_id = "test_id";

        when(productRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.getProductById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getProductById should throw custom exception productNotFound when given id does not exist")
    @Test
    void testGetProductById_productNotFound() {
        String test_id = "test_id";

        when(productRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> productService.getProductById(test_id));

    }

    @DisplayName("getProductByName should return valid product when given name is exist")
    @Test
    void testGetProductByName_success() {
        String test_name = "test_name";

        when(productRepositoryMock.findByName(test_name)).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.getProductByName(test_name);

        assertNotNull(result);
    }

    @DisplayName("getProductByName should throw custom exception productNotFound when given name does not exist")
    @Test
    void testGetProductByName_productNotFound() {
        String test_name = "test_name";

        when(productRepositoryMock.findByName(test_name)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> productService.getProductByName(test_name));

    }

    @DisplayName("getProductsByStatus should return list product when given with status ")
    @Test
    void testGetProductsByStatus_success() {
        ProductStatusEnums status = ProductStatusEnums.WAITING;
        Product newProduct = new Product("test_id_2", "test_name_2", "test_food_id_2", "test_thumbnailId", 100.0, ProductStatusEnums.WAITING);
        List<Product> productList = new ArrayList<>();
        productList.add(newProduct);
        productList.add(productMock);

        when(productRepositoryMock.findAllByStatusOrderByUpdateDateDesc(status)).thenReturn(productList);

        List<Product> result = productService.getAllProductsByStatus(status);

        assertEquals(2, result.size());
    }

    @DisplayName("createProduct should return valid product when given food name in input is exist")
    @Test
    void testCreateProduct_success() {
        CreateProductInput createProductInput = new CreateProductInput("test_food_name", 100.0);
        Food dbFood = new Food();
        dbFood.setId("test_food_id");
        dbFood.setName("test_food_name");
        dbFood.setReady(true);
        dbFood.setRecipeId("test_recipe_id");

        when(foodServiceMock.getFoodByName(createProductInput.getName().toLowerCase())).thenReturn(dbFood);
        when(productRepositoryMock.save(any(Product.class))).thenReturn(productMock);

        Product result = productService.createProduct(createProductInput);

        assertNotNull(result);
        assertEquals("test_food_name", result.getName());
        assertEquals(100.0, result.getPrice());
        assertEquals("test_food_id", productMock.getFoodId());

        verify(foodServiceMock).makeFood(dbFood.getName());
    }

    @DisplayName("createProduct should throw foodNotFound when given food name in input does not exist")
    @Test
    void testCreateProduct_foodNotFound() {
        CreateProductInput createProductInput = new CreateProductInput("test_food_name", 100.0);

        when(foodServiceMock.getFoodByName(createProductInput.getName().toLowerCase())).thenThrow(CustomException.foodNotFound());

        assertThrows(CustomException.class, () -> productService.createProduct(createProductInput));

    }

    @Test
    void testMakeFoodAsync_Success() {

        String foodName = "food_name";

        productService.makeFoodAsync(foodName);

    }

    @Test
    void testMakeFoodAsync_Failure() {

        String foodName = "food_name";

        when(foodServiceMock.makeFood(foodName)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            productService.makeFoodAsync(foodName);
        });
    }

    @DisplayName("updateProductPrice should return product when given id in exist and price greater than zero at updateProductPriceInput")
    @Test
    void testUpdateProductPrice_success(){
        UpdateProductPriceInput updateProductPriceInput = new UpdateProductPriceInput("test_product_price",10.0);

        when(productRepositoryMock.findById(updateProductPriceInput.getId())).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.updateProductPrice(updateProductPriceInput);

        assertEquals(10.0,result.getPrice());
    }

    @DisplayName("updateProductPrice should throw productNotFound exception when given id does not exist and price greater than zero at updateProductPriceInput")
    @Test
    void testUpdateProductPrice_productNotFound(){
        UpdateProductPriceInput updateProductPriceInput = new UpdateProductPriceInput("test_product_price",10.0);

        when(productRepositoryMock.findById(updateProductPriceInput.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()->productService.updateProductPrice(updateProductPriceInput));

    }

    @DisplayName("updateProductPrice should throw productPriceLimit exception when given id exist and price  equals or less than zero at updateProductPriceInput")
    @Test
    void testUpdateProductPrice_productPriceLimit(){
        UpdateProductPriceInput updateProductPriceInput = new UpdateProductPriceInput("test_product_price",0.0);

        when(productRepositoryMock.findById(updateProductPriceInput.getId())).thenReturn(Optional.ofNullable(productMock));

        assertThrows(CustomException.class,()->productService.updateProductPrice(updateProductPriceInput));

    }
}
