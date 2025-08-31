package com.codegym.service;

import com.codegym.model.CartItem;
import com.codegym.model.User;
import com.codegym.model.Product;
import com.codegym.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> findByUser(User user) {
        return cartItemRepository.findByUser(user);
    }

    public CartItem findByUserAndProduct(User user, Product product) {
        return cartItemRepository.findByUserAndProduct(user, product);
    }

    public CartItem save(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
