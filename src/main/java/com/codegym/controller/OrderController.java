package com.codegym.controller;

import com.codegym.model.CartItem;
import com.codegym.model.Order;
import com.codegym.model.User;
import com.codegym.service.CartService;
import com.codegym.service.OrderService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    // Thanh toán
    @PostMapping("/orders/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<CartItem> cartItems = cartService.getCart(user);

        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Giỏ hàng trống, không thể thanh toán!");
            return "redirect:/cart";
        }

        Order order = orderService.createOrder(user, cartItems);
        cartService.clearCart(user); // Xóa giỏ hàng

        redirectAttributes.addFlashAttribute("success", "🎉 Thanh toán thành công! Đơn hàng #" + order.getId());
        return "redirect:/orders/" + order.getId();
    }

    // Chi tiết đơn hàng
    @GetMapping("/orders/{id}")
    public String orderDetail(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long id,
                              Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        Order order = orderService.findByUser(user).stream()
                .filter(o -> o.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (order == null) {
            return "redirect:/orders";
        }

        model.addAttribute("order", order);
        return "orders/detail"; // cần có file detail.html
    }

    // Lịch sử đơn hàng
    @GetMapping("/orders")
    public String orderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        List<Order> orders = orderService.findByUser(user);
        model.addAttribute("orders", orders);
        return "orders/list"; // cần có file user-list.html
    }
}
