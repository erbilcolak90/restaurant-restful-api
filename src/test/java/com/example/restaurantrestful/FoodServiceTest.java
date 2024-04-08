package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.*;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.UnitTypeEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.jpa.FoodRepository;
import com.example.restaurantrestful.service.FoodService;
import com.example.restaurantrestful.service.IngredientService;
import com.example.restaurantrestful.service.RecipeService;
import com.example.restaurantrestful.service.StockService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.restaurantrestful.service.FoodService.convertQuantity;
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

    @Mock
    private StockService stockServiceMock;

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
        when(foodRepositoryMock.findByRecipeId(anyString())).thenReturn(Optional.ofNullable(foodMock));

        List<Food> result = foodServiceMock.getFoodsByContainsIngredient(test_name);

        assertNotNull(result);
        assertEquals(1,result.size());
    }

    @DisplayName("getFoodsByContainsIngredient throw custom exception foodNotFound when given ingredient name is exist and recipe contains ingredient but food not found with recipe Id")
    @Test
    void testGetFoodsByContainsIngredient_foodNotFound(){
        String test_name = "test_name";
        ingredientMock = new Ingredient("id", "test_ingredient_name", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG);
        List<String> recipeIds= new ArrayList<>();
        recipeIds.add("test_recipe_id");
        when(ingredientServiceMock.getIngredientByName(test_name)).thenReturn(ingredientMock);
        when(recipeServiceMock.getRecipeIdsByContainsIngredient(ingredientMock.getId())).thenReturn(recipeIds);
        when(foodRepositoryMock.findByRecipeId(anyString())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,()->foodServiceMock.getFoodsByContainsIngredient(test_name));

        assertEquals("Food not found",exception.getMessage());
    }

    @DisplayName("updateFoodStatus should true when given food id is exist and is ready already false")
    @Test
    void testUpdateFoodStatus_success(){
        String test_id = "test_id";

        when(foodRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(foodMock));

        when(foodRepositoryMock.save(any(Food.class))).thenReturn(foodMock);

        boolean result = foodServiceMock.updateFoodStatus(test_id);

        assertTrue(result);
    }

    @DisplayName("updateFoodStatus should false when given food id is exist and is ready already true")
    @Test
    void testUpdateFoodStatus_false(){
        String test_id = "test_id";
        foodMock.setReady(true);
        when(foodRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(foodMock));

        boolean result = foodServiceMock.updateFoodStatus(test_id);

        assertFalse(result);
    }

    @DisplayName("updateFoodStatus should throw custom exception foodNotFound when given food id does not exist")
    @Test
    void testUpdateFoodStatus_foodNotFound(){
        String test_id = "test_id";

        when(foodRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,()-> foodServiceMock.updateFoodStatus(test_id));

        assertEquals("Food not found",exception.getMessage());
    }

    @DisplayName("makeFood should return valid food when given name is exist on recipe and ingredientItem is exist on stock")
    @Test
    void testMakeFood_success(){
        String foodName = "test_name";
        Recipe dbRecipe = new Recipe();
        dbRecipe.setId("test_recipe_id");
        dbRecipe.setName("test_name");
        dbRecipe.setIngredientListItem(new ArrayList<>());

        IngredientListItem item = new IngredientListItem();
        item.setId("test_ingredientListItem_id1");
        item.setRecipeId("test_recipe_id");
        item.setIngredientId("test_ingredient_id");
        item.setIngredientType(IngredientTypeEnums.BAKERY.toString());
        item.setUnit(UnitTypeEnums.GR.toString());
        item.setQuantity(100.0);
        IngredientListItem item2 = new IngredientListItem();
        item2.setId("test_ingredientListItem_id2");
        item2.setRecipeId("test_recipe_id");
        item2.setIngredientId("test_ingredient_id2");
        item2.setIngredientType(IngredientTypeEnums.FRUIT.toString());
        item2.setUnit(UnitTypeEnums.GR.toString());
        item2.setQuantity(4000.0);

        dbRecipe.getIngredientListItem().add(item);
        dbRecipe.getIngredientListItem().add(item2);

        Stock stockMock = new Stock();
        stockMock.setId("test_stock_id1");
        stockMock.setIngredientId("test_ingredient_id1");
        stockMock.setQuantity(5.0);
        stockMock.setType(IngredientTypeEnums.BAKERY.toString());
        stockMock.setUnit(UnitTypeEnums.KG.toString());
        stockMock.setExpireDate(new Date());

        Stock stockMock2 = new Stock();
        stockMock2.setId("test_stock_id2");
        stockMock2.setIngredientId("test_ingredient_id2");
        stockMock2.setQuantity(3.0);
        stockMock2.setType(IngredientTypeEnums.FRUIT.toString());
        stockMock2.setUnit(UnitTypeEnums.KG.toString());
        stockMock2.setExpireDate( new Date());

        Stock stockMock3 = new Stock();
        stockMock3.setId("test_stock_id2");
        stockMock3.setIngredientId("test_ingredient_id2");
        stockMock3.setQuantity(1.0);
        stockMock3.setType(IngredientTypeEnums.FRUIT.toString());
        stockMock3.setUnit(UnitTypeEnums.KG.toString());
        stockMock3.setExpireDate(new Date());


        when(recipeServiceMock.getRecipeByName(foodName.toLowerCase())).thenReturn(dbRecipe);
        when(stockServiceMock.findNearestExpirationStockByItemId(item.getIngredientId())).thenReturn(stockMock); // Örnek stok verileri
        when(stockServiceMock.findNearestExpirationStockByItemId(item2.getIngredientId())).thenReturn(stockMock3); // Örnek stok verileri
        when(stockServiceMock.findNearestExpirationStockByItemId(item2.getIngredientId())).thenReturn(stockMock2); // Örnek stok verileri

        // Act
        Food createdFood = foodServiceMock.makeFood(foodName);

        // Assert
        assertNotNull(createdFood);
        assertEquals("test_name", createdFood.getName());
        assertTrue(createdFood.isReady());

    }

    @Test
    void testConvertQuantity() {
        // KG to GR
        double quantityKGtoGR = 0.5;
        UnitTypeEnums sourceUnitKGtoGR = UnitTypeEnums.KG;
        UnitTypeEnums targetUnitKGtoGR = UnitTypeEnums.GR;

        double convertedQuantityKGtoGR = convertQuantity(quantityKGtoGR, sourceUnitKGtoGR, targetUnitKGtoGR);

        assertEquals(500, convertedQuantityKGtoGR);

        // MIL to LT
        double quantityMILtoLT = 2000;
        UnitTypeEnums sourceUnitMILtoLT = UnitTypeEnums.MIL;
        UnitTypeEnums targetUnitMILtoLT = UnitTypeEnums.LT;

        double convertedQuantityMILtoLT = convertQuantity(quantityMILtoLT, sourceUnitMILtoLT, targetUnitMILtoLT);

        assertEquals(2, convertedQuantityMILtoLT);

        // LT to MIL
        double quantityLTtoMIL = 3;
        UnitTypeEnums sourceUnitLTtoMIL = UnitTypeEnums.LT;
        UnitTypeEnums targetUnitLTtoMIL = UnitTypeEnums.MIL;

        double convertedQuantityLTtoMIL = convertQuantity(quantityLTtoMIL, sourceUnitLTtoMIL, targetUnitLTtoMIL);

        assertEquals(3000, convertedQuantityLTtoMIL);
    }

    @Test
    void testConvertQuantity_nullUnits() {
        assertThrows(NullPointerException.class, () -> {
            convertQuantity(100, null, UnitTypeEnums.KG);
        });

        assertThrows(NullPointerException.class, () -> {
            convertQuantity(100, UnitTypeEnums.GR, null);
        });

        assertThrows(NullPointerException.class, () -> {
            convertQuantity(100, null, null);
        });
    }

    @Test
    void testConvertQuantity_zeroOrNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(0, UnitTypeEnums.GR, UnitTypeEnums.KG);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(-100, UnitTypeEnums.GR, UnitTypeEnums.KG);
        });
    }

    @Test
    void testConvertQuantity_invalidUnitConversion() {
        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.GR, UnitTypeEnums.LT);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.GR, UnitTypeEnums.MIL);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.KG, UnitTypeEnums.LT);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.KG, UnitTypeEnums.MIL);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.MIL, UnitTypeEnums.GR);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.MIL, UnitTypeEnums.KG);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.LT, UnitTypeEnums.GR);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            convertQuantity(100, UnitTypeEnums.LT, UnitTypeEnums.KG);
        });
    }


    @AfterEach
    void tearDown() {

    }


}
