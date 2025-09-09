package com.codegym.service;

import com.codegym.model.CartItem;
import com.codegym.model.Product;
import com.codegym.model.User;
import com.codegym.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getCart(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addToCart(User user,Product product, int quantity) {
        cartItemRepository.findByUserAndProduct_Id(user, product.getId())
                .ifPresentOrElse(
                        existing -> {
                            existing.setQuantity(existing.getQuantity() + quantity);
                            cartItemRepository.save(existing);
                        },
                        () -> {
                            CartItem newItem = new CartItem();
                            newItem.setUser(user);
                            newItem.setProduct(product);
                            newItem.setQuantity(quantity);
                            cartItemRepository.save(newItem);
                        }
                );
    }

    public void updateQuantity(Long cartItemId, int quantity) {
        cartItemRepository.findById(cartItemId).ifPresent(item -> {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        });
    }

    public void removeItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    public void clearCart(User user) {
        List<CartItem> items = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(items);
    }

    public double getTotal(User user) {
        return getCart(user).stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }
}
