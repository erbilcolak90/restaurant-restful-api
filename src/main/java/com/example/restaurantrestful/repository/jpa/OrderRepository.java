package com.example.restaurantrestful.repository.jpa;

import com.example.restaurantrestful.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String > {

    List<Order> findByIsCompletedFalse();

    List<Order> findByCreateDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<Order> findByIdAndIsDeletedFalse(String id);
}
