package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.IngredientRepository;
import com.example.restaurantrestful.service.IngredientService;
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
class IngredientServiceTest {

    @Mock
    private IngredientRepository ingredientRepositoryMock;

    @InjectMocks
    private IngredientService ingredientServiceMock;

    private Ingredient ingredientMock;

    @BeforeEach()
    void setUp(){
        ingredientMock = new Ingredient();
        ingredientMock.setId("test_id");
        ingredientMock.setName("test_ingredient_name");
        ingredientMock.setType(IngredientTypeEnums.BAKERY);
        ingredientMock.setUnit(UnitTypeEnums.KG);
        ingredientMock.setDeleted(false);

    }

    @DisplayName("getIngredientById should return a valid Ingredient for given input")
    @Test
    void testGetIngredientById_success(){
        String test_id ="test_id";
        when(ingredientRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(ingredientMock));

        Ingredient result = ingredientServiceMock.getIngredientById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getIngredientById should throw custom exception ingredientNotFound for given input")
    @Test
    void testGetIngredientById_ingredientNotFound(){
        String test_id = "test_id";

        when(ingredientRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        CustomException result = assertThrows(CustomException.class,()-> ingredientServiceMock.getIngredientById(test_id),CustomException.ingredientNotFound().getMessage());

        assertEquals("Ingredient not found",result.getMessage());

    }

    @AfterEach
    void tearDown() {

    }
}