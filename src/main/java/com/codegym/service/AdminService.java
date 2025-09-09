package com.codegym.service;

import com.codegym.model.CartItem;
import com.codegym.model.Order;
import com.codegym.model.OrderStatus;
import com.codegym.repository.CartItemRepository;
import com.codegym.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AdminService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAllWithUserAndProduct();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        return orderRepository.findById(orderId).map(order -> {
            order.setStatus(status);
            return orderRepository.save(order);
        }).orElse(null);
    }
}
