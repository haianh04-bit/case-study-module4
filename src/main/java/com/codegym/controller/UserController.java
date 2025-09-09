package com.codegym.controller;

import com.codegym.dto.UserDTO;
import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 🔹 Trang profile cá nhân
    @GetMapping("/profile")
    public String profile(HttpSession session, Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        UserDTO dto = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getImageUrl()
        );

        // Lưu user vào session
        session.setAttribute("currentUser", user);

        model.addAttribute("user", dto);
        return "users/profile"; // => users/profile.html
    }

    // 🔹 Form chỉnh sửa thông tin cá nhân
    @GetMapping("/edit")
    public String editForm(Authentication authentication, Model model) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        UserDTO dto = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getImageUrl()
        );
        model.addAttribute("user", dto);
        return "users/edit"; // => users/edit.html
    }

    // 🔹 Submit chỉnh sửa profile
    @PostMapping("/edit")
    public String updateProfile(Authentication authentication,
                                @Valid @ModelAttribute("user") UserDTO dto, BindingResult result,
                                @RequestParam("avatarFile") MultipartFile avatarFile, HttpSession session,
                                Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return "users/edit"; // Trả lại form kèm lỗi
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user == null) return "redirect:/login";

        userService.updateUser(user.getId(), dto, avatarFile);
        Optional<User> updatedUser = userService.findById(user.getId());
        session.setAttribute("currentUser", updatedUser);
        return "redirect:/user/profile?success";
    }
}
