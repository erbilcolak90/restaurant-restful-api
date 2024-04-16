package com.example.restaurantrestful;

import com.example.restaurantrestful.entity.Menu;
import com.example.restaurantrestful.repository.elastic.MenuRepository;
import com.example.restaurantrestful.service.MenuService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    private MenuService menuServiceMock;

    @Mock
    private MenuRepository menuRepositoryMock;

    private Menu menuMock;

    @BeforeEach
    void setUp(){

    }

    @AfterEach
    void tearDown(){

    }
}
