package com.codegym.service;

import com.codegym.model.*;
import com.codegym.repository.OrderRepository;
import com.codegym.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public Order createOrder(User user, List<CartItem> cartItems) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        double total = cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
        order.setTotalPrice(total);

        order = orderRepository.save(order);

        // Lưu OrderDetails
        for (CartItem item : cartItems) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            detail.setPrice(item.getProduct().getPrice()); // giá tại thời điểm đặt hàng
            orderDetailRepository.save(detail);
        }

        return order; // ✅ Trả về order đã lưu
    }

    public List<Order> findByUser(User user) {
        return orderRepository.findByUser_Id(user.getId());
    }

}
