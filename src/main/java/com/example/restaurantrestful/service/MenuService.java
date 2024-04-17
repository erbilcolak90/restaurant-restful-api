package com.example.restaurantrestful.service;

import com.example.restaurantrestful.dto.inputs.menu.AddProductToMenuInput;
import com.example.restaurantrestful.dto.inputs.menu.ChangeMenuNameInput;
import com.example.restaurantrestful.dto.inputs.menu.CreateMenuInput;
import com.example.restaurantrestful.dto.inputs.menu.DeleteProductFromMenuInput;
import com.example.restaurantrestful.dto.payloads.MenuPayload;
import com.example.restaurantrestful.entity.Menu;
import com.example.restaurantrestful.entity.Product;
import com.example.restaurantrestful.exception.CustomException;
import com.example.restaurantrestful.repository.elastic.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, ProductService productService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
    }

    public MenuPayload getMenuById(String id) {
        return MenuPayload.convert(menuRepository.findById(id).orElseThrow(CustomException::menuNotFound));
    }

    public List<MenuPayload> getAllMenus() {
        Iterable<Menu> menuIterable = menuRepository.findAll();

        List<MenuPayload> menuPayloads = new ArrayList<>();

        menuIterable.forEach(menu -> {
            MenuPayload payload = MenuPayload.convert(menu);
            menuPayloads.add(payload);
        });

        return menuPayloads;
    }

    @Transactional
    public MenuPayload createMenu(CreateMenuInput createMenuInput) {
        Menu dbMenu = menuRepository.findByName(createMenuInput.getName().toLowerCase()).orElse(null);

        if (dbMenu != null) {
            throw CustomException.menuNameIsAlreadyExist();
        } else {
            dbMenu = new Menu();
            dbMenu.setProducts(new ArrayList<>());
            dbMenu.setName(createMenuInput.getName().toLowerCase());

            for (String productId : createMenuInput.getProductIds()) {
                Product dbProduct = productService.getProductById(productId);
                dbMenu.getProducts().add(dbProduct);
            }

           dbMenu = menuRepository.save(dbMenu);

            return MenuPayload.convert(dbMenu);
        }
    }

    @Transactional
    public MenuPayload addProductToMenu(AddProductToMenuInput addProductToMenuInput){
        Menu dbMenu = menuRepository.findById(addProductToMenuInput.getMenuId()).orElseThrow(CustomException::menuNotFound);
        Product dbProduct = productService.getProductById(addProductToMenuInput.getProductId());

        if(!dbMenu.getProducts().contains(dbProduct)){
            dbMenu.getProducts().add(dbProduct);
            dbMenu.setUpdateDate(new Date());
            menuRepository.save(dbMenu);

            return MenuPayload.convert(dbMenu);
        }else{
            throw CustomException.productIsAlreadyExistInMenu();
        }
    }

    @Transactional
    public MenuPayload deleteProductToMenu(DeleteProductFromMenuInput deleteProductFromMenuInput){
        Menu dbMenu = menuRepository.findById(deleteProductFromMenuInput.getMenuId()).orElseThrow(CustomException::menuNotFound);
        Product dbProduct = productService.getProductById(deleteProductFromMenuInput.getProductId());

        if(dbMenu.getProducts().contains(dbProduct)){
            dbMenu.getProducts().remove(dbProduct);
            dbMenu.setUpdateDate(new Date());
            menuRepository.save(dbMenu);

            return MenuPayload.convert(dbMenu);
        }else{
            throw CustomException.productDoesNotExistInMenu();
        }

    }

    @Transactional
    public MenuPayload changeMenuName(ChangeMenuNameInput changeMenuNameInput){
        Menu dbMenu = menuRepository.findById(changeMenuNameInput.getMenuId()).orElseThrow(CustomException::menuNotFound);
        Menu isExistName = menuRepository.findByName(changeMenuNameInput.getNewName().toLowerCase()).orElse(null);

        if(isExistName == null){
            dbMenu.setName(changeMenuNameInput.getNewName().toLowerCase());
            dbMenu.setUpdateDate(new Date());
            menuRepository.save(dbMenu);

            return MenuPayload.convert(dbMenu);
        }else{
            throw CustomException.menuNameIsAlreadyExist();
        }
    }
}
