package com.example.restaurantrestful;

import com.example.restaurantrestful.dto.payloads.MenuPayload;
import com.example.restaurantrestful.entity.Menu;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.MenuRepository;
import com.example.restaurantrestful.service.MenuService;
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
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuServiceMock;

    @Mock
    private MenuRepository menuRepositoryMock;

    private Menu menuMock;

    @BeforeEach
    void setUp(){
        menuMock = new Menu();
        menuMock.setId("test_id");
        menuMock.setName("test_menu_name");

    }

    @DisplayName("getMenuById should return valid menuPayload when given id is exist")
    @Test
    void testGetMenuById_success(){
        String test_id = "test_id";

        when(menuRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(menuMock));

        MenuPayload result = menuServiceMock.getMenuById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getMenuById should throw custom exception menuNotFound when given id does not exist")
    @Test
    void testGetMenuById_menuNotFound(){

        when(menuRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, ()-> menuServiceMock.getMenuById(anyString()));
    }

    @DisplayName("getAllMenus should return list menuPayload")
    @Test
    void testGetAllMenu_success(){
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
        assertEquals(3,result.size());

    }

    @AfterEach
    void tearDown(){

    }
}
