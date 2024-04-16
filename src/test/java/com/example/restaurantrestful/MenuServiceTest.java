package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.inputs.menu.AddProductToMenuInput;
import com.example.restaurantrestful.dto.inputs.menu.CreateMenuInput;
import com.example.restaurantrestful.dto.inputs.menu.DeleteProductFromMenuInput;
import com.example.restaurantrestful.dto.payloads.MenuPayload;
import com.example.restaurantrestful.entity.Menu;
import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.enums.ProductStatusEnums;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.MenuRepository;
import com.example.restaurantrestful.service.MenuService;
import com.example.restaurantrestful.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @InjectMocks
    private MenuService menuServiceMock;

    @Mock
    private MenuRepository menuRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    private Menu menuMock;

    private Product productMock;

    @BeforeEach
    void setUp() {
        menuMock = new Menu();
        menuMock.setId("test_id");
        menuMock.setName("test_menu_name");
        menuMock.setProducts(new ArrayList<>());

        productMock = new Product("test_product_id", "test_product_name", "", "", 100.0, ProductStatusEnums.READY);

    }

    @DisplayName("getMenuById should return valid menuPayload when given id is exist")
    @Test
    void testGetMenuById_success() {
        String test_id = "test_id";

        when(menuRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(menuMock));

        MenuPayload result = menuServiceMock.getMenuById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getMenuById should throw custom exception menuNotFound when given id does not exist")
    @Test
    void testGetMenuById_menuNotFound() {

        when(menuRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> menuServiceMock.getMenuById(anyString()));
    }

    @DisplayName("getAllMenus should return list menuPayload")
    @Test
    void testGetAllMenu_success() {
        Menu menu1 = new Menu();
        Menu menu2 = new Menu();
        Menu menu3 = new Menu();
        menu1.setId("test_menu_id_1");
        menu1.setName("test_menu_name_1");
        menu2.setId("test_menu_id_2");
        menu2.setName("test_menu_name_2");
        menu3.setId("test_menu_id_3");
        menu3.setName("test_menu_name_3");
        List<Menu> menuList = new ArrayList<>();
        menuList.add(menu1);
        menuList.add(menu2);
        menuList.add(menu3);

        when(menuRepositoryMock.findAll()).thenReturn(menuList);

        List<MenuPayload> result = menuServiceMock.getAllMenus();

        assertNotNull(result);
        assertEquals(3, result.size());

    }

    @DisplayName("createMenu should return menuPayload when given menu name does not exist in createMenuInput")
    @Test
    void testCreateMenu_success() {
        String menuName = "test_menu_name";
        List<String> productIds = Arrays.asList("1");
        CreateMenuInput createMenuInput = new CreateMenuInput(menuName, productIds);

        Product product = new Product("test_product_id", "test_product_name", "", "", 100.0, ProductStatusEnums.READY);
        menuMock.getProducts().add(product);

        when(menuRepositoryMock.findByName(menuName.toLowerCase())).thenReturn(Optional.empty());
        when(productServiceMock.getProductById(anyString())).thenReturn(product);
        when(menuRepositoryMock.save(any(Menu.class))).thenReturn(menuMock);

        MenuPayload result = menuServiceMock.createMenu(createMenuInput);

        assertNotNull(result);
        assertEquals(menuName.toLowerCase(), result.getName());
        assertEquals(productIds.size(), result.getProducts().size());
    }

    @DisplayName("createMenu should throw custom exception menuNameIsAlreadyExist when given menu name is exist in createMenuInput")
    @Test
    void testCreateMenu_menuNameIsAlreadyExist() {
        String menuName = "test_menu_name";
        List<String> productIds = Arrays.asList("1");
        CreateMenuInput createMenuInput = new CreateMenuInput(menuName, productIds);

        when(menuRepositoryMock.findByName(createMenuInput.getName().toLowerCase())).thenReturn(Optional.ofNullable(menuMock));

        assertThrows(CustomException.class, () -> menuServiceMock.createMenu(createMenuInput));
    }

    @DisplayName("addProductToMenu should return valid menuPayload when given menu id is exist and productId is exist in addProductToMenuInput")
    @Test
    void testAddProductToMenu_success() {
        AddProductToMenuInput addProductToMenuInput = new AddProductToMenuInput("test_id", "test_product_id");
        Product product = new Product("test_product_id", "test_product_name", "", "", 100.0, ProductStatusEnums.READY);

        when(menuRepositoryMock.findById(addProductToMenuInput.getMenuId())).thenReturn(Optional.ofNullable(menuMock));
        when(productServiceMock.getProductById(addProductToMenuInput.getProductId())).thenReturn(product);

        MenuPayload result = menuServiceMock.addProductToMenu(addProductToMenuInput);

        assertEquals(1, menuMock.getProducts().size());
    }

    @DisplayName("addProductToMenu should throw custom exception menuNotFound when given menu id does not exist in addProductToMenuInput")
    @Test
    void testAddProductToMenu_menuNotFound() {
        AddProductToMenuInput addProductToMenuInput = new AddProductToMenuInput("test_id", "test_product_id");

        when(menuRepositoryMock.findById(addProductToMenuInput.getMenuId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> menuServiceMock.addProductToMenu(addProductToMenuInput));

        assertEquals("Menu not found", exception.getMessage());
    }

    @DisplayName("addProductToMenu should throw custom exception productIsAlreadyExistInMenu when given menu id is exist and product id exist in menuProducts list in addProductToMenuInput")
    @Test
    void testAddProductToMenu_productIsAlreadyExistInMenu() {
        AddProductToMenuInput addProductToMenuInput = new AddProductToMenuInput("test_id", "test_product_id");
        Product product = new Product("test_product_id", "test_product_name", "", "", 100.0, ProductStatusEnums.READY);
        menuMock.getProducts().add(productMock);

        when(menuRepositoryMock.findById(addProductToMenuInput.getMenuId())).thenReturn(Optional.ofNullable(menuMock));
        when(productServiceMock.getProductById(addProductToMenuInput.getProductId())).thenReturn(product);

        CustomException exception = assertThrows(CustomException.class, () -> menuServiceMock.addProductToMenu(addProductToMenuInput));

        assertEquals("Product is already exist in menu", exception.getMessage());
    }

    @DisplayName("deleteProductToMenu should return menuPayload when given id and product id is exist in deleteProductFromMenuInput")
    @Test
    void testDeleteProductToMenu_success(){
        DeleteProductFromMenuInput deleteProductFromMenuInput = new DeleteProductFromMenuInput("test_id","test_product_id");
        Product product = new Product("test_product_id", "test_product_name", "", "", 100.0, ProductStatusEnums.READY);
        menuMock.getProducts().add(product);

        when(menuRepositoryMock.findById(deleteProductFromMenuInput.getMenuId())).thenReturn(Optional.ofNullable(menuMock));
        when(productServiceMock.getProductById(deleteProductFromMenuInput.getProductId())).thenReturn(product);

        MenuPayload result = menuServiceMock.deleteProductToMenu(deleteProductFromMenuInput);

        assertEquals(0, menuMock.getProducts().size());
    }

    @DisplayName("deleteProductToMenu should throw custom exception menuNotFound when given menu id does not exist in deleteProductFromMenuInput")
    @Test
    void testDeleteProductToMenu_menuNotFound(){
        DeleteProductFromMenuInput deleteProductFromMenuInput = new DeleteProductFromMenuInput("test_id","test_product_id");

        when(menuRepositoryMock.findById(deleteProductFromMenuInput.getMenuId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> menuServiceMock.deleteProductToMenu(deleteProductFromMenuInput));

        assertEquals("Menu not found", exception.getMessage());
    }

    @DisplayName("deleteProductToMenu should throw custom exception productDoesNotExistInMenu when given menu id is exist and product id does not exist in menuProducts at deleteProductToMenuInput")
    @Test
    void testDeleteProductToMenu_productDoesNotExistInMenu(){
        DeleteProductFromMenuInput deleteProductFromMenuInput = new DeleteProductFromMenuInput("test_id","test_product_id");

        when(menuRepositoryMock.findById(deleteProductFromMenuInput.getMenuId())).thenReturn(Optional.ofNullable(menuMock));
        when(productServiceMock.getProductById(deleteProductFromMenuInput.getProductId())).thenReturn(productMock);

        CustomException exception = assertThrows(CustomException.class, ()-> menuServiceMock.deleteProductToMenu(deleteProductFromMenuInput));

        assertEquals("Product does not exist in menu",exception.getMessage());
    }

    @AfterEach
    void tearDown() {

    }
}
