package com.codegym.repository;

import com.codegym.model.CartItem;
import com.codegym.model.User;
import com.codegym.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}
