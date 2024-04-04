package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.ingredientlistitem.CreateIngredientListItemInput;
import com.example.restaurantrestful.dto.inputs.ingredientlistitem.UpdateIngredientListItemInput;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.IngredientListItem;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.IngredientListItemRepository;
import com.example.restaurantrestful.repository.elastic.RecipeRepository;
import com.example.restaurantrestful.repository.jpa.IngredientRepository;
import com.example.restaurantrestful.service.IngredientListItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientListItemServiceTest {

    @InjectMocks
    private IngredientListItemService ingredientListItemServiceMock;
    @Mock
    private IngredientListItemRepository ingredientListItemRepositoryMock;
    @Mock
    private RecipeRepository recipeRepositoryMock;
    @Mock
    private IngredientRepository ingredientRepositoryMock;
    private Recipe recipeMock;
    private IngredientListItem ingredientListItemMock;
    private Ingredient ingredientMock;


    @BeforeEach
    void setUp() {
        ingredientListItemMock = new IngredientListItem();
        ingredientListItemMock.setId("test_id");
        ingredientListItemMock.setRecipeId("test_recipe_id");
        ingredientListItemMock.setIngredientId("test_ingredient_id");
        ingredientListItemMock.setIngredientType(IngredientTypeEnums.BAKERY.toString());
        ingredientListItemMock.setUnit(UnitTypeEnums.GR.toString());
        ingredientListItemMock.setQuantity(400.0);

        recipeMock = new Recipe();
        recipeMock.setId("test_recipe_id");
        recipeMock.setName("test_recipe_name");
        recipeMock.setIngredientListItem(Collections.singletonList(ingredientListItemMock));

        ingredientMock = new Ingredient();
        ingredientMock.setId("test_ingredient_id");
        ingredientMock.setName("test_ingredient_name");
        ingredientMock.setType(IngredientTypeEnums.BAKERY);
        ingredientMock.setUnit(UnitTypeEnums.KG);
        ingredientMock.setDeleted(false);

    }

    @DisplayName("getIngredientListItemById should return valid ingredientListItem when given id is exist")
    @Test
    void testGetIngredientListItemById_success() {
        String id = "test_id";

        when(ingredientListItemRepositoryMock.findById(id)).thenReturn(Optional.ofNullable(ingredientListItemMock));

        IngredientListItem result = ingredientListItemServiceMock.getIngredientListItemById(id);

        assertNotNull(result);

    }

    @DisplayName("getIngredientListItemById should throw custom exception ingredientListItemNotFound when given id does not exist")
    @Test
    void testGetIngredientListItemById_ingredientListItemNotFound() {
        String id = "test_id";

        when(ingredientListItemRepositoryMock.findById(id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> ingredientListItemServiceMock.getIngredientListItemById(id));

        assertEquals("Ingredient list item not found", exception.getMessage());

    }

    @DisplayName("getIngredientListItemByRecipeId should return list ingredientListItem when given recipeId is exist in recipeDB")
    @Test
    void testGetIngredientListItemByRecipeId_success() {
        String recipeId = "test_recipe_id";
        List<IngredientListItem> itemList = new ArrayList<>();
        itemList.add(ingredientListItemMock);

        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.ofNullable(recipeMock));
        when(ingredientListItemRepositoryMock.findIngredientListItemsByContainsRecipeId(recipeId)).thenReturn(itemList);

        List<IngredientListItem> result = ingredientListItemServiceMock.getIngredientListItemsByRecipeId(recipeId);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @DisplayName("getIngredientListItemByRecipeId should throw custom exception recipeNotFound When given recipeId is does not exist in recipeDB")
    @Test
    void testGetIngredientListItemByRecipeId_recipeNotFound() {
        String recipeId = "test_recipe_id";

        when(recipeRepositoryMock.findById(recipeId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> ingredientListItemServiceMock.getIngredientListItemsByRecipeId(recipeId));

        assertEquals("Recipe not found", exception.getMessage());
    }

    @DisplayName("createIngredientListItem should return valid ingredientListItem when ingredientId and recipeId is exist in given createIngredientListItemInput")
    @Test
    void testCreateIngredientListItem_success() {
        CreateIngredientListItemInput createIngredientListItemInput = new CreateIngredientListItemInput("test_ingredient_id", "test_recipe_id", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG, 100.0);

        when(ingredientRepositoryMock.findById(createIngredientListItemInput.getIngredientId())).thenReturn(Optional.ofNullable(ingredientMock));
        when(recipeRepositoryMock.findById(createIngredientListItemInput.getRecipeId())).thenReturn(Optional.ofNullable(recipeMock));
        when(ingredientListItemRepositoryMock.save(any(IngredientListItem.class))).thenReturn(ingredientListItemMock);

        IngredientListItem result = ingredientListItemServiceMock.createIngredientListItem(createIngredientListItemInput);

        assertEquals("test_ingredient_id",result.getIngredientId());
        assertEquals("test_recipe_id",result.getRecipeId());

    }

    @DisplayName("createIngredientListItem should throw custom exception ingredientNotFound when ingredientId does not exist in given createIngredientListItemInput")
    @Test
    void testCreateIngredientListItem_ingredientNotFound() {
        CreateIngredientListItemInput createIngredientListItemInput = new CreateIngredientListItemInput("test_ingredient_id", "test_recipe_id", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG, 100.0);

        when(ingredientRepositoryMock.findById(createIngredientListItemInput.getIngredientId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> ingredientListItemServiceMock.createIngredientListItem(createIngredientListItemInput));

        assertEquals("Ingredient not found", exception.getMessage());

    }

    @DisplayName("createIngredientListItem should throw custom exception recipeNotFound when ingredientId is exist recipeId does not exist in given createIngredientListItemInput")
    @Test
    void testCreateIngredientListItem_recipeNotFound() {
        CreateIngredientListItemInput createIngredientListItemInput = new CreateIngredientListItemInput("test_ingredient_id", "test_recipe_id", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG, 100.0);

        when(ingredientRepositoryMock.findById(createIngredientListItemInput.getIngredientId())).thenReturn(Optional.ofNullable(ingredientMock));
        when(recipeRepositoryMock.findById(createIngredientListItemInput.getRecipeId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> ingredientListItemServiceMock.createIngredientListItem(createIngredientListItemInput));

        assertEquals("Recipe not found", exception.getMessage());

    }

    @DisplayName("updateIngredientListItem should return valid ingredientListItem when given updateIngredientListItemInput")
    @Test
    void testUpdateIngredientListItem_success(){
        UpdateIngredientListItemInput updateIngredientListItemInput = new UpdateIngredientListItemInput("test_id","test_ingredient_id","test_recipe_id",IngredientTypeEnums.CHEESE,UnitTypeEnums.KG,400.0);

        when(ingredientListItemRepositoryMock.findById(updateIngredientListItemInput.getIngredientListItemId())).thenReturn(Optional.ofNullable(ingredientListItemMock));
        when(recipeRepositoryMock.existsById(updateIngredientListItemInput.getRecipeId())).thenReturn(true);
        when(ingredientRepositoryMock.existsById(updateIngredientListItemInput.getIngredientId())).thenReturn(true);

        IngredientListItem result = ingredientListItemServiceMock.updateIngredientListItem(updateIngredientListItemInput);

        assertEquals(400,result.getQuantity());
        assertEquals(IngredientTypeEnums.CHEESE.toString(),result.getIngredientType());

    }

    @DisplayName("updateIngredientListItem should throw custom exception ingredientListItemNotFound when id in updateIngredientListItem does not exist")
    @Test
    void testUpdateIngredientListItem_ingredientListItemNotFound() {
        UpdateIngredientListItemInput updateIngredientListItemInput = new UpdateIngredientListItemInput("test_id","test_ingredient_id","test_recipe_id",IngredientTypeEnums.CHEESE,UnitTypeEnums.KG,400.0);

        when(ingredientListItemRepositoryMock.findById(updateIngredientListItemInput.getIngredientListItemId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> ingredientListItemServiceMock.updateIngredientListItem(updateIngredientListItemInput));

        assertEquals("Ingredient list item not found", exception.getMessage());

    }


}
