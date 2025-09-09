package com.codegym.controller;

import com.codegym.model.Product;
import com.codegym.model.User;
import com.codegym.service.CartService;
import com.codegym.service.ProductService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String viewCart(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmail(userDetails.getUsername());
        model.addAttribute("cartItems", cartService.getCart(user));
        model.addAttribute("total", cartService.getTotal(user));
        return "cart/view";
    }

    @PostMapping("/add/{productId}")
    public String addToCart(@AuthenticationPrincipal UserDetails userDetails,
                            @PathVariable Long productId,
                            @RequestParam(defaultValue = "1") int quantity) {
        User user = userService.findByEmail(userDetails.getUsername());
        Product product = productService.findCartId(productId);
        if (product != null) {
            cartService.addToCart(user, product, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update/{cartItemId}")
    public String updateCart(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long cartItemId,
                             @RequestParam int quantity) {
        cartService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{cartItemId}")
    public String removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        cartService.clearCart(user);
        return "redirect:/cart";
    }
}
