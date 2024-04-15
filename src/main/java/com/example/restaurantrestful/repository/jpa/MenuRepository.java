package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, String> {
}
