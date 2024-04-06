package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Food;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.FoodRepository;
import com.example.restaurantrestful.service.FoodService;
import com.example.restaurantrestful.service.IngredientService;
import com.example.restaurantrestful.service.RecipeService;
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
class FoodServiceTest {

    @InjectMocks
    private FoodService foodServiceMock;

    @Mock
    private FoodRepository foodRepositoryMock;

    @Mock
    private RecipeService recipeServiceMock;

    @Mock
    private IngredientService ingredientServiceMock;

    private Food foodMock;

    private Ingredient ingredientMock;

    @BeforeEach
    void setUp() {
        foodMock = new Food();
        foodMock.setId("test_id");
        foodMock.setName("test_name");
        foodMock.setRecipeId("test_recipe_id");
        foodMock.setReady(false);

    }

    @DisplayName("getFoodById should return valid Food when given id is exist")
    @Test
    void testGetFoodById_success() {
        String test_id = "test_id";

        when(foodRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(foodMock));

        Food result = foodServiceMock.getFoodById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getFoodById should throw custom exception foodNotFound when given id does not exist")
    @Test
    void testGetFoodById_foodNotFound() {
        String test_id = "test_id";

        when(foodRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,()->foodServiceMock.getFoodById(test_id));

        assertEquals("Food not found",exception.getMessage());
    }

    @DisplayName("getFoodByName should return valid Food when given name is exist")
    @Test
    void testGetFoodByName_success() {
        String test_name = "test_name";

        when(foodRepositoryMock.findByName(test_name)).thenReturn(Optional.ofNullable(foodMock));

        Food result = foodServiceMock.getFoodByName(test_name);

        assertNotNull(result);
    }

    @DisplayName("getFoodByName should throw custom exception foodNotFound when given name does not exist")
    @Test
    void testGetFoodByName_foodNotFound() {
        String test_name = "test_name";

        when(foodRepositoryMock.findByName(test_name)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,()->foodServiceMock.getFoodByName(test_name));

        assertEquals("Food not found",exception.getMessage());
    }

    @DisplayName("getFoodsByContainsIngredient return list foot when given ingredient name is exist and recipe contains ingredient")
    @Test
    void testGetFoodsByContainsIngredient_success(){
        String test_name = "test_name";
        ingredientMock = new Ingredient("id", "test_ingredient_name", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG);
        List<String> recipeIds= new ArrayList<>();
        recipeIds.add("test_recipe_id");
        when(ingredientServiceMock.getIngredientByName(test_name)).thenReturn(ingredientMock);
        when(recipeServiceMock.getRecipeIdsByContainsIngredient(ingredientMock.getId())).thenReturn(recipeIds);
        when(foodRepositoryMock.findByRecipeId(anyString())).thenReturn(foodMock);

        List<Food> result = foodServiceMock.getFoodsByContainsIngredient(test_name);

        assertNotNull(result);
        assertEquals(1,result.size());
    }


    @AfterEach
    void tearDown() {

    }


}
