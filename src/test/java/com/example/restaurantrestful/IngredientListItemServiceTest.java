package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.IngredientListItem;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import com.example.restaurantrestful.service.IngredientListItemService;
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
class IngredientListItemServiceTest {

    @InjectMocks
    private IngredientListItemService ingredientListItemServiceMock;

    @Mock
    private IngredientListItemRepository ingredientListItemRepositoryMock;

    private IngredientListItem ingredientListItemMock;

    @BeforeEach
    void setUp(){
        ingredientListItemMock = new IngredientListItem();
        ingredientListItemMock.setId("test_id");
        ingredientListItemMock.setRecipeId("test_recipe_id");
        ingredientListItemMock.setIngredientId("test_ingredient_id");
        ingredientListItemMock.setIngredientType(IngredientTypeEnums.BAKERY.toString());
        ingredientListItemMock.setUnit(UnitTypeEnums.GR.toString());
        ingredientListItemMock.setQuantity(400.0);

    }

    @DisplayName("getIngredientListItemById should return valid ingredientListItem when given id is exist")
    @Test
    void testGetIngredientListItemById_success(){
        String id = "test_id";

        when(ingredientListItemRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(ingredientListItemMock));

        IngredientListItem result = ingredientListItemServiceMock.getIngredientListItemById(id);

        assertNotNull(result);

    }

    @DisplayName("getIngredientListItemById should throw custom exception ingredientListItemNotFound when given id does not exist")
    @Test
    void testGetIngredientListItemById_ingredientListItemNotFound(){
        String id = "test_id";

        when(ingredientListItemRepositoryMock.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,()->ingredientListItemServiceMock.getIngredientListItemById(id));

        assertEquals("Ingredient list item not found",exception.getMessage());

    }
}
