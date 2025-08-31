package com.codegym.controller;

import com.codegym.dto.CategoryDTO;
import com.codegym.dto.CreateProductDTO;
import com.codegym.dto.ProductDTO;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.service.CategoryService;
import com.codegym.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(@RequestParam(value = "categoryId", required = false) Long categoryId,
                               @PageableDefault(size = 8) Pageable pageable,
                               Model model) {
        Page<ProductDTO> products;

        if (categoryId != null) {
            Category category = (Category) categoryService.getAllCatgory();
            products = productService.findByCategory(category, pageable);
            model.addAttribute("categoryId", categoryId);
        } else {
            products = productService.findAll(pageable);
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categoryService.getAllCatgory());
        return "products/list";
    }

    @GetMapping("/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Optional<ProductDTO> product = productService.findByIdDB(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "products/view";
        } else {
            return "redirect:/products?error=notfound";
        }
    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new CreateProductDTO());
        model.addAttribute("categories", categoryService.getAllCatgory());
        return "products/create";
    }

    // Xử lý thêm sản phẩm
    @PostMapping("/create")
    public String createProduct(@ModelAttribute CreateProductDTO createProductDTO) throws IOException {
        productService.createProduct(createProductDTO);
        return "redirect:/products";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id)  {
        productService.deleteById(id);
        return "redirect:/products";
    }

    // Hiển thị form update
    @GetMapping("/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            Product products = product.get();
            CreateProductDTO dto = new CreateProductDTO();
            dto.setName(products.getName());
            dto.setDescription(products.getDescription());
            dto.setPrice(products.getPrice());
            dto.setCategoryId(products.getCategory() != null ? products.getCategory().getId() : null);

            model.addAttribute("product", dto);
            model.addAttribute("productId", products.getId()); // để biết đang sửa sản phẩm nào
            model.addAttribute("categories", categoryService.getAllCatgory());
            model.addAttribute("productImage", products.getImageUrl());
            return "products/edit";
        } else {
            return "redirect:/products?error=notfound";
        }
    }

    // Xử lý update
    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @ModelAttribute CreateProductDTO updateDTO) throws IOException {
        productService.updateProduct(id, updateDTO);
        return "redirect:/products";
    }

}