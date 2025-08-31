package com.codegym.repository;

import com.codegym.model.Order;
import com.codegym.model.User;
import com.codegym.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByStatus(OrderStatus status);
}
