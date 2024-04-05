package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.recipe.CreateRecipeInput;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.entity.IngredientListItem;
import com.example.restaurantrestful.entity.Recipe;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.RecipeRepository;
import com.example.restaurantrestful.service.IngredientListItemService;
import com.example.restaurantrestful.service.IngredientService;
import com.example.restaurantrestful.service.RecipeService;
import com.example.restaurantrestful.service.StockService;
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
class RecipeServiceTest {

    @InjectMocks
    private RecipeService recipeServiceMock;

    @Mock
    private RecipeRepository recipeRepositoryMock;

    @Mock
    private IngredientListItemService ingredientListItemServiceMock;

    @Mock
    private StockService stockServiceMock;

    @Mock
    private IngredientService ingredientServiceMock;

    private Recipe recipeMock;

    private IngredientListItem ingredientListItemMock;

    @BeforeEach
    void setUp() {
        ingredientListItemMock = new IngredientListItem();
        ingredientListItemMock.setId("test_1");
        ingredientListItemMock.setIngredientId("test_ingredient_id_1");
        ingredientListItemMock.setIngredientType(IngredientTypeEnums.BAKERY.toString());
        ingredientListItemMock.setUnit(UnitTypeEnums.KG.toString());
        ingredientListItemMock.setQuantity(100.0);

        recipeMock = new Recipe();
        recipeMock.setId("test_id");
        recipeMock.setName("test_name");
        recipeMock.setIngredientListItem(Collections.singletonList(ingredientListItemMock));

    }

    @DisplayName("getRecipeById should return valid recipe when given id is exist")
    @Test
    void testGetRecipeById_success() {
        String test_id = "test_id";

        when(recipeRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(recipeMock));

        Recipe result = recipeServiceMock.getRecipeById(test_id);

        assertNotNull(result);
        assertEquals(1, result.getIngredientListItem().size());

    }

    @DisplayName("getRecipeById should throw custom exception recipeNotFound exception when given id does not exist")
    @Test
    void testGetRecipeById_recipeNotFound() {
        String test_id = "test_id";

        when(recipeRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> recipeServiceMock.getRecipeById(test_id));

        assertEquals("Recipe not found", exception.getMessage());

    }

    @DisplayName("getRecipeByContainsIngredient should return list recipe when given id is exist")
    @Test
    void testGetRecipeByContainsIngredient_success() {
        String test_id = "test_ingredient_id_1";
        Ingredient dbIngredient = new Ingredient("test_ingredient_id_1", "test_name", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG);
        List<Recipe> recipeList = new ArrayList<>();
        recipeList.add(recipeMock);

        when(ingredientServiceMock.getIngredientById(test_id)).thenReturn(dbIngredient);
        when(recipeRepositoryMock.findByIngredientId(test_id)).thenReturn(recipeList);

        List<Recipe> result = recipeServiceMock.getRecipeByContainsIngredient(test_id);

        assertEquals(1, result.size());
    }

    @DisplayName("getRecipeByContainsIngredient should throw custom exception ingredient not found when given id does not exist")
    @Test
    void testGetRecipeByContainsIngredient_ingredientNotFound() {
        String test_id = "test_id";

        when(ingredientServiceMock.getIngredientById(test_id)).thenThrow(CustomException.ingredientNotFound());

        CustomException exception = assertThrows(CustomException.class, () -> recipeServiceMock.getRecipeByContainsIngredient(test_id));

        assertEquals("Ingredient not found", exception.getMessage());
    }

    @DisplayName("getRecipeByName should return valid recipe when given name is exist")
    @Test
    void testGetRecipeByName_success() {
        String test_name = "test_name";

        when(recipeRepositoryMock.findByName(test_name)).thenReturn(Optional.ofNullable(recipeMock));

        Recipe result = recipeServiceMock.getRecipeByName(test_name);

        assertNotNull(result);
        assertEquals(1, result.getIngredientListItem().size());

    }

    @DisplayName("getRecipeByName should throw custom exception recipeNotFound exception when given name does not exist")
    @Test
    void testGetRecipeByName_recipeNotFound() {
        String test_name = "test_name";

        when(recipeRepositoryMock.findByName(test_name)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> recipeServiceMock.getRecipeByName(test_name));

        assertEquals("Recipe not found", exception.getMessage());

    }

    @DisplayName("createRecipe should return valid Recipe when given name in createRecipeInput")
    @Test
    void testCreateRecipe_success() {
        CreateRecipeInput createRecipeInput = new CreateRecipeInput("test_name");
        recipeMock.setIngredientListItem(new ArrayList<>());

        when(recipeRepositoryMock.findByName(createRecipeInput.getName().toLowerCase())).thenReturn(Optional.empty());
        when(recipeRepositoryMock.save(any(Recipe.class))).thenReturn(recipeMock);

        Recipe result = recipeServiceMock.createRecipe(createRecipeInput);

        assertNotNull(result);
        assertEquals("test_name", result.getName());
    }

    @DisplayName("createRecipe should throw custom exception recipeNameIsAlreadyExist when given name in createRecipeInput")
    @Test
    void testCreateRecipe_recipeNameIsAlreadyExist() {
        CreateRecipeInput createRecipeInput = new CreateRecipeInput("test_name");
        recipeMock.setIngredientListItem(null);

        when(recipeRepositoryMock.findByName(createRecipeInput.getName().toLowerCase())).thenReturn(Optional.ofNullable(recipeMock));

        CustomException exception = assertThrows(CustomException.class, () -> recipeServiceMock.createRecipe(createRecipeInput));

        assertEquals("Recipe name is already exist", exception.getMessage());
    }

    @DisplayName("addIngredientsToRecipe should return recipe when given recipe id is exist")
    @Test
    void testAddIngredientsToRecipe_success(){
        String test_id = "test_id";
        List<IngredientListItem> newIngredientListItem = new ArrayList<>();
        newIngredientListItem.add(new IngredientListItem("test_id_1","test_id","test_ingredient_id_1",IngredientTypeEnums.CHEESE.toString(),UnitTypeEnums.KG.toString(),4.0));
        newIngredientListItem.add(new IngredientListItem("test_id_2","test_id","test_ingredient_id_2",IngredientTypeEnums.BAKERY.toString(),UnitTypeEnums.KG.toString(),4.0));

        when(recipeRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(recipeMock));
        when(ingredientListItemServiceMock.getIngredientListItemsByRecipeId(test_id)).thenReturn(newIngredientListItem);
        when(recipeRepositoryMock.save(any(Recipe.class))).thenReturn(recipeMock);

        Recipe result = recipeServiceMock.addIngredientsToRecipe(test_id);

        assertEquals(2,result.getIngredientListItem().size());
    }

    @DisplayName("addIngredientsToRecipe should throw custom exception recipeNotFound when given recipe id does not exist")
    @Test
    void testAddIngredientsToRecipe_recipeNotFound(){
        String test_id = "test_id";

        when(recipeRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> recipeServiceMock.addIngredientsToRecipe(test_id));

        assertEquals("Recipe not found", exception.getMessage());
    }

}
