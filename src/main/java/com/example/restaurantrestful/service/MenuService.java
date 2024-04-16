package com.example.restaurantrestful.service;

import com.example.restaurantrestful.entity.Menu;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu getMenuById(String id){
        return menuRepository.findById(id).orElseThrow(CustomException::menuNotFound);
    }
}
