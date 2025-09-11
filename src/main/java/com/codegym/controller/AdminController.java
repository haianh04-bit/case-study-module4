package com.codegym.controller;

import com.codegym.model.CartItem;
import com.codegym.model.Order;
import com.codegym.model.OrderStatus;
import com.codegym.service.AdminService;
import com.codegym.service.DashboardService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;
    private final DashboardService dashboardService;

    public AdminController(UserService userService, AdminService adminService, DashboardService dashboardService) {
        this.userService = userService;
        this.adminService = adminService;
        this.dashboardService = dashboardService;
    }
    // Danh sách user
    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/admin/user-list";
    }

    // Xoá user
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/carts")
    public String listAllCarts(Model model) {
        List<CartItem> allCartItems = adminService.getAllCartItems();
        model.addAttribute("cartItems", allCartItems);
        return "admin/cart-list";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = adminService.getAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("statuses", OrderStatus.values()); // để hiển thị dropdown
        return "admin/order-list";
    }

    @PostMapping("orders/{id}/update-status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        adminService.updateOrderStatus(id, status);
        return "redirect:/admin/orders";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsers", dashboardService.getTotalUsers());
        model.addAttribute("totalOrders", dashboardService.getTotalOrders());
        model.addAttribute("totalProducts", dashboardService.getTotalProducts());
        return "admin/dashboard";
    }
}
