package com.example.restaurantrestful.service;

import com.example.restaurantrestful.repository.jpa.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository, MenuProductService menuProductService) {
        this.menuRepository = menuRepository;
        this.menuProductService = menuProductService;
    }
}
