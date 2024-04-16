package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.payloads.MenuPayload;
import com.example.restaurantrestful.entity.Menu;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuPayload getMenuById(String id){
        return MenuPayload.convert(menuRepository.findById(id).orElseThrow(CustomException::menuNotFound));
    }

    public List<MenuPayload> getAllMenus(){
        Iterable<Menu> menuIterable = menuRepository.findAll();

        List<MenuPayload> menuPayloads = new ArrayList<>();

        menuIterable.forEach(menu -> {
            MenuPayload payload = MenuPayload.convert(menu);
            menuPayloads.add(payload);
        });

        return menuPayloads;
    }
}
