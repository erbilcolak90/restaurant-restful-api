package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.product.*;
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
        UpdateProductPriceInput updateProductPriceInput = new UpdateProductPriceInput("test_product_id",10.0);

        when(productRepositoryMock.findById(updateProductPriceInput.getId())).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.updateProductPrice(updateProductPriceInput);

        assertEquals(10.0,result.getPrice());
    }

    @DisplayName("updateProductPrice should throw productNotFound exception when given id does not exist and price greater than zero at updateProductPriceInput")
    @Test
    void testUpdateProductPrice_productNotFound(){
        UpdateProductPriceInput updateProductPriceInput = new UpdateProductPriceInput("test_product_id",10.0);

        when(productRepositoryMock.findById(updateProductPriceInput.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()->productService.updateProductPrice(updateProductPriceInput));

    }

    @DisplayName("updateProductPrice should throw productPriceLimit exception when given id exist and price  equals or less than zero at updateProductPriceInput")
    @Test
    void testUpdateProductPrice_productPriceLimit(){
        UpdateProductPriceInput updateProductPriceInput = new UpdateProductPriceInput("test_product_id",0.0);

        when(productRepositoryMock.findById(updateProductPriceInput.getId())).thenReturn(Optional.ofNullable(productMock));

        assertThrows(CustomException.class,()->productService.updateProductPrice(updateProductPriceInput));

    }

    @DisplayName("updateProductStatus should return product when given id in exist and status is not same with exist product at updateProductStatusInput")
    @Test
    void testUpdateProductStatus_success(){
        UpdateProductStatusInput updateProductStatusInput = new UpdateProductStatusInput("test_product_id",ProductStatusEnums.READY);

        when(productRepositoryMock.findById(updateProductStatusInput.getId())).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.updateProductStatus(updateProductStatusInput);

        assertEquals(ProductStatusEnums.READY,result.getStatus());
    }

    @DisplayName("updateProductStatus should throw custom exception productNotFound when given id does not exist updateProductStatusInput")
    @Test
    void testUpdateProductStatus_productNotFound(){
        UpdateProductStatusInput updateProductStatusInput = new UpdateProductStatusInput("test_product_id",ProductStatusEnums.READY);

        when(productRepositoryMock.findById(updateProductStatusInput.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()->productService.updateProductStatus(updateProductStatusInput));

    }

    @DisplayName("updateProductStatus should throw custom exception productStatusIsSameWithInput exception when given id is exist but status is same with exist product status updateProductStatusInput")
    @Test
    void testUpdateProductStatus_productStatusIsSameWithInput(){
        UpdateProductStatusInput updateProductStatusInput = new UpdateProductStatusInput("test_product_id",ProductStatusEnums.WAITING);

        when(productRepositoryMock.findById(updateProductStatusInput.getId())).thenReturn(Optional.ofNullable(productMock));

        CustomException exception = assertThrows(CustomException.class,()->productService.updateProductStatus(updateProductStatusInput));

        assertEquals("Product status is same with given input status",exception.getMessage());

    }

    @DisplayName("updateProductThumbnail should return product when given id in exist and thumbnail id is not same with exist product at updateProductThumbnailInput")
    @Test
    void testUpdateProductThumbnail_success(){
        UpdateProductThumbnailInput updateProductThumbnailInput = new UpdateProductThumbnailInput("test_product_id","test_thumbnail_id2");

        when(productRepositoryMock.findById(updateProductThumbnailInput.getId())).thenReturn(Optional.ofNullable(productMock));

        Product result = productService.updateProductThumbnail(updateProductThumbnailInput);

        assertEquals("test_thumbnail_id2",result.getThumbnailId());
    }

    @DisplayName("updateProductThumbnail should throw custom exception productNotFound when given id does not exist at updateProductThumbnailInput")
    @Test
    void testUpdateProductThumbnail_productNotFound(){
        UpdateProductThumbnailInput updateProductThumbnailInput = new UpdateProductThumbnailInput("test_product_id","test_thumbnail_id");

        when(productRepositoryMock.findById(updateProductThumbnailInput.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()->productService.updateProductThumbnail(updateProductThumbnailInput));

    }

    @DisplayName("updateProductThumbnail should throw custom exception productThumbnailIdSameWithInput exception when given id is exist but thumbnail id  is same with exist product thumbnailId at updateProductThumbnailInput")
    @Test
    void testUpdateProductThumbnail_productThumbnailIdSameWithInput(){
        UpdateProductThumbnailInput updateProductThumbnailInput = new UpdateProductThumbnailInput("test_product_id","test_thumbnail_id");

        when(productRepositoryMock.findById(updateProductThumbnailInput.getId())).thenReturn(Optional.ofNullable(productMock));

        CustomException exception = assertThrows(CustomException.class,()->productService.updateProductThumbnail(updateProductThumbnailInput));

        assertEquals("Thumbnail id is same with given input",exception.getMessage());

    }

    @DisplayName("updateProductName should return valid product when given id is exist on product db and given name is exist on food db")
    @Test
    void testUpdateProductName_success(){
        UpdateProductNameInput updateProductNameInput = new UpdateProductNameInput("test_product_id","test_new_name");
        Food dbFood = new Food();
        dbFood.setId("test_food_id");
        dbFood.setName("test_new_name");

        when(productRepositoryMock.findById(updateProductNameInput.getId())).thenReturn(Optional.ofNullable(productMock));
        when(foodServiceMock.getFoodByName(updateProductNameInput.getName().toLowerCase())).thenReturn(dbFood);

        Product result = productService.updateProductName(updateProductNameInput);

        assertEquals(dbFood.getName(),productMock.getName());
    }

    @DisplayName("updateProductName should throw custom exception productNotFound when given id does not exist at updateProductNameInput")
    @Test
    void testUpdateProductName_productNotFound(){
        UpdateProductNameInput updateProductNameInput = new UpdateProductNameInput("test_product_id","test_new_name");

        when(productRepositoryMock.findById(updateProductNameInput.getId())).thenReturn(Optional.empty());

        assertThrows(CustomException.class,()-> productService.updateProductName(updateProductNameInput));
    }

    @DisplayName("updateProductName should throw custom exception foodNotFound when given id is exist but name does not exist on food db at updateProductNameInput")
    @Test
    void testUpdateProductName_foodNotFound(){
        UpdateProductNameInput updateProductNameInput = new UpdateProductNameInput("test_product_id","test_new_name");

        when(productRepositoryMock.findById(updateProductNameInput.getId())).thenReturn(Optional.ofNullable(productMock));
        when(foodServiceMock.getFoodByName(updateProductNameInput.getName().toLowerCase())).thenThrow(CustomException.foodNotFound());

        assertThrows(CustomException.class,()-> productService.updateProductName(updateProductNameInput));
    }

}
