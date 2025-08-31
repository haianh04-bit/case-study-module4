package com.codegym.controller;

import com.codegym.dto.CategoryDTO;
import com.codegym.model.Category;
import com.codegym.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getAllCatgory();
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    // Show form create
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/create";
    }

    // Save new category
    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/categories";
    }

    // Hiển thị form edit
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        if (categoryDTO == null) {
            return "redirect:/categories";
        }
        model.addAttribute("category", categoryDTO);
        return "categories/edit";
    }


    // Update category
    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id, @ModelAttribute Category category) {
        category.setId(id);
        categoryService.save(category);
        return "redirect:/categories";
    }

    // Delete category
    @GetMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return "redirect:/categories";
    }
}
