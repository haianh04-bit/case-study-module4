package com.codegym.repository;

import com.codegym.model.CartItem;
import com.codegym.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct_Id(User user, Long productId);
    @Query("SELECT c FROM CartItem c JOIN FETCH c.user u JOIN FETCH c.product p")
    List<CartItem> findAllWithUserAndProduct();
}
