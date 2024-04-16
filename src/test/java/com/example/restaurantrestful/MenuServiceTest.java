package com.example.restaurantrestful;

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

    @DisplayName("getMenuById should return valid menu when given id is exist")
    @Test
    void testGetMenuById_success(){
        String test_id = "test_id";

        when(menuRepositoryMock.findById(test_id)).thenReturn(Optional.ofNullable(menuMock));

        Menu result = menuServiceMock.getMenuById(test_id);

        assertNotNull(result);
    }

    @DisplayName("getMenuById should throw custom exception menuNotFound when given id does not exist")
    @Test
    void testGetMenuById_menuNotFound(){

        when(menuRepositoryMock.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, ()-> menuServiceMock.getMenuById(anyString()));
    }

    @AfterEach
    void tearDown(){

    }
}
