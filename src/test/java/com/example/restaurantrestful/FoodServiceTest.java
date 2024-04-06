package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Food;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.FoodRepository;
import com.example.restaurantrestful.service.FoodService;
import com.example.restaurantrestful.service.RecipeService;
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
class FoodServiceTest {

    @InjectMocks
    private FoodService foodServiceMock;

    @Mock
    private FoodRepository foodRepositoryMock;

    @Mock
    private RecipeService recipeServiceMock;

    private Food foodMock;

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


}
