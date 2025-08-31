package com.codegym.controller;

import com.codegym.dto.UserDTO;
import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Xem profile
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        UserDTO dto = new UserDTO(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPhone(), user.getAddress(), user.getImageUrl()
        );
        model.addAttribute("user", dto);
        return "users/profile";
    }

    // Form update profile
    @GetMapping("/edit")
    public String editForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        UserDTO dto = new UserDTO(
                user.getId(), user.getUsername(), user.getEmail(),
                user.getPhone(), user.getAddress(), user.getImageUrl()
        );
        model.addAttribute("users", dto);
        return "users/edit";
    }

    // Lưu update
    @PostMapping("/edit")
    public String updateProfile(Authentication authentication,
                                @ModelAttribute("user") UserDTO dto,
                                @RequestParam("avatarFile") MultipartFile avatarFile) throws IOException {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());

        // upload avatar
        if (!avatarFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
            String uploadDir = "uploads/avatars/";
            java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);
            if (!java.nio.file.Files.exists(uploadPath)) {
                java.nio.file.Files.createDirectories(uploadPath);
            }
            java.nio.file.Path filePath = uploadPath.resolve(fileName);
            avatarFile.transferTo(filePath.toFile());

            user.setImageUrl("/" + uploadDir + fileName);
        }

        userService.save(user);
        return "redirect:/user/profile?success";
    }
}
