package com.codegym.controller;

import com.codegym.model.Role;
import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private UserService userService;

    // Danh sách user
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/users/list";
    }

    // Xoá user
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
