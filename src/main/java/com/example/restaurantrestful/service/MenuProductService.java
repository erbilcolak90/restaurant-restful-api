package com.example.restaurantrestful.service;

import com.example.restaurantrestful.repository.jpa.MenuProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;

    public MenuProductService(MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }
}
