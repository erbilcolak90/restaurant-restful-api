package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String > {

    @Query("SELECT * FROM Orders WHERE is_completed = false")
    List<Order> findByIsCompletedFalse();

    List<Order> findByCreateDateBetween(LocalDate startDate, LocalDate endDate);
}
