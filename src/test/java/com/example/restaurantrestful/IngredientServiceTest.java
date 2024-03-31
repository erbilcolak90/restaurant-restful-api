package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.ingredient.CreateIngredientInput;
import com.example.restaurantrestful.dto.inputs.ingredient.GetAllIngredientsInput;
import com.example.restaurantrestful.dto.inputs.ingredient.UpdateIngredientInput;
import com.example.restaurantrestful.entity.Ingredient;
import com.example.restaurantrestful.enums.IngredientTypeEnums;
import com.example.restaurantrestful.enums.SortBy;
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
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    void setUp() {
        ingredientMock = new Ingredient();
        ingredientMock.setId("test_id");
        ingredientMock.setName("test_ingredient_name");
        ingredientMock.setType(IngredientTypeEnums.BAKERY);
        ingredientMock.setUnit(UnitTypeEnums.KG);
        ingredientMock.setDeleted(false);

    }

    @DisplayName("getIngredientById should return a valid Ingredient for given input")
    @Test
    void testGetIngredientById_success() {
        String test_id = "test_id";
        when(ingredientRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(ingredientMock));

        Ingredient result = ingredientServiceMock.getIngredientById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getIngredientById should throw custom exception ingredientNotFound for given input")
    @Test
    void testGetIngredientById_ingredientNotFound() {
        String test_id = "test_id";

        when(ingredientRepositoryMock.findById(test_id)).thenReturn(Optional.empty());

        CustomException result = assertThrows(CustomException.class, () -> ingredientServiceMock.getIngredientById(test_id), CustomException.ingredientNotFound().getMessage());

        assertEquals("Ingredient not found", result.getMessage());

    }

    @DisplayName("getIngredientByName should return a valid Ingredient for given input")
    @Test
    void testGetIngredientByName_success() {
        String test_name = ingredientMock.getName().toLowerCase();

        when(ingredientRepositoryMock.findByName(test_name)).thenReturn(Optional.ofNullable(ingredientMock));

        Ingredient result = ingredientServiceMock.getIngredientByName(test_name.toLowerCase());

        assertNotNull(result);
        assertEquals(test_name, result.getName());

    }

    @DisplayName("getIngredientByName should throw custom exception ingredientNameNotFound for given input")
    @Test
    void testGetIngredientByName_ingredientNameNotFound() {
        String test_name = ingredientMock.getName().toLowerCase();

        when(ingredientRepositoryMock.findByName(test_name)).thenReturn(Optional.empty());

        CustomException resultException = assertThrows(CustomException.class, () -> ingredientServiceMock.getIngredientByName(test_name));

        assertEquals("Ingredient name not found", resultException.getMessage());
    }

    @DisplayName("getAllIngredients should return page list ingredient with GetAllIngredientsInput")
    @Test
    void testGetAllIngredients_success() {
        GetAllIngredientsInput getAllIngredientsInput = new GetAllIngredientsInput(1, 1, "id", SortBy.ASC);
        List<Ingredient> ingredientList = new ArrayList<>();
        Pageable pageable = PageRequest.of(1, 10, Sort.Direction.ASC, "id");
        ingredientList.add(ingredientMock);
        Page<Ingredient> ingredientPage = new PageImpl<>(ingredientList, pageable, ingredientList.size());

        when(ingredientRepositoryMock.findAll(any(Pageable.class))).thenReturn(ingredientPage);

        Page<Ingredient> result = ingredientServiceMock.getAllIngredients(getAllIngredientsInput);
        assertNotNull(result);
    }

    @DisplayName("getAllIngredients should return empty page when no ingredients found")
    @Test
    void testGetAllIngredients_noIngredientsFound() {
        GetAllIngredientsInput getAllIngredientsInput = new GetAllIngredientsInput(1, 1, "id", SortBy.ASC);

        when(ingredientRepositoryMock.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<Ingredient> result = ingredientServiceMock.getAllIngredients(getAllIngredientsInput);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @DisplayName("createIngredient should return valid ingredient with input createIngredientInput")
    @Test
    void testCreateIngredient_success() {
        CreateIngredientInput createIngredientInput = new CreateIngredientInput("test_name", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG);

        when(ingredientRepositoryMock.findByName(createIngredientInput.getName())).thenReturn(Optional.empty());
        when(ingredientRepositoryMock.save(any(Ingredient.class))).thenReturn(ingredientMock);

        Ingredient result = ingredientServiceMock.createIngredient(createIngredientInput);

        assertNotNull(result);
    }

    @DisplayName("createIngredient should throw custom exception ingredient name is already exist for given createIngredientInput")
    @Test
    void testCreateIngredient_ingredientNameIsAlreadyExist() {
        CreateIngredientInput createIngredientInput = new CreateIngredientInput("test_ingredient_name", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG);

        when(ingredientRepositoryMock.findByName(createIngredientInput.getName())).thenReturn(Optional.ofNullable(ingredientMock));

        CustomException exceptionResult = assertThrows(CustomException.class, () -> ingredientServiceMock.createIngredient(createIngredientInput));

        assertEquals("Ingredient name is already exist", exceptionResult.getMessage());
    }

    @DisplayName("updateIngredient should return valid ingredient for given input")
    @Test
    void testUpdateIngredient_success() {
        UpdateIngredientInput updateIngredientInput = new UpdateIngredientInput("test_id", "test_name", IngredientTypeEnums.CHEESE, UnitTypeEnums.GR);
        String name = Objects.requireNonNull(updateIngredientInput.getName()).toLowerCase();

        when(ingredientRepositoryMock.findById(updateIngredientInput.getId())).thenReturn(Optional.ofNullable(ingredientMock));
        when(ingredientRepositoryMock.findByName(name)).thenReturn(Optional.empty());

        Ingredient result = ingredientServiceMock.updateIngredient(updateIngredientInput);

        assertEquals(name, result.getName());
        assertEquals(updateIngredientInput.getType(), result.getType());
        assertEquals(updateIngredientInput.getUnit(), result.getUnit());
    }

    @DisplayName("updateIngredient should throw custom exception ingredientNotFound when id from given input is not exist on database")
    @Test
    void testUpdateIngredient_ingredientIsNotFound() {
        UpdateIngredientInput updateIngredientInput = new UpdateIngredientInput("test_id", "test_update_name", IngredientTypeEnums.CHEESE, UnitTypeEnums.GR);

        when(ingredientRepositoryMock.findById(updateIngredientInput.getId())).thenReturn(Optional.empty());

        CustomException result = assertThrows(CustomException.class, () -> ingredientServiceMock.updateIngredient(updateIngredientInput));

        assertEquals("Ingredient not found", result.getMessage());
    }

    @DisplayName("updateIngredient should throw custom exception ingredientNameIsAlreadyExist when name field from given input is match with different ingredient on DB")
    @Test
    void testUpdateIngredient_ingredientNameIsAlreadyExist() {
        UpdateIngredientInput updateIngredientInput = new UpdateIngredientInput("test_id", "test_ingredient_name", IngredientTypeEnums.CHEESE, UnitTypeEnums.GR);
        Ingredient secondIngredient = new Ingredient("id", "test_ingredient_name", IngredientTypeEnums.BAKERY, UnitTypeEnums.KG);
        String name = Objects.requireNonNull(updateIngredientInput.getName()).toLowerCase();

        when(ingredientRepositoryMock.findById(updateIngredientInput.getId())).thenReturn(Optional.ofNullable(ingredientMock));
        when(ingredientRepositoryMock.findByName(name)).thenReturn(Optional.of(secondIngredient));

        CustomException result = assertThrows(CustomException.class, () -> ingredientServiceMock.updateIngredient(updateIngredientInput));

        assertEquals("Ingredient name is already exist", result.getMessage());
    }

    @DisplayName("deleteIngredient should return String Ingredient id : {id} + successfully deleted with given input id")
    @Test
    void testDeleteIngredient_success() {
        String test_id = "test_id";

        when(ingredientRepositoryMock.findById(test_id.toLowerCase())).thenReturn(Optional.ofNullable(ingredientMock));

        String result = ingredientServiceMock.deleteIngredient(test_id);

        assertEquals("Ingredient id : " + ingredientMock.getId() + " successfully deleted", result);
    }

    @DisplayName("deleteIngredient should throw custom exception ingredientIsNotFound with given input id")
    @Test
    void testDeleteIngredient_ingredientNotFound() {
        String test_id = "test_id";

        when(ingredientRepositoryMock.findById(test_id.toLowerCase())).thenReturn(Optional.empty());

        CustomException result = assertThrows(CustomException.class, () -> ingredientServiceMock.deleteIngredient(test_id));

        assertEquals("Ingredient not found", result.getMessage());
    }

    @DisplayName("deleteIngredient should throw custom exception ingredientIsAlreadyDeleted with given input id")
    @Test
    void testDeleteIngredient_ingredientIsAlreadyDeleted() {
        String test_id = "test_id";
        ingredientMock.setDeleted(true);
        when(ingredientRepositoryMock.findById(test_id.toLowerCase())).thenReturn(Optional.ofNullable(ingredientMock));

        CustomException result = assertThrows(CustomException.class, () -> ingredientServiceMock.deleteIngredient(test_id));

        assertEquals("Ingredient is already deleted", result.getMessage());
    }

    @AfterEach
    void tearDown() {

    }
}