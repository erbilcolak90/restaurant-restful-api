package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, String> {
}
