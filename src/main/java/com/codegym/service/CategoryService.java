package com.codegym.service;

import com.codegym.dto.CategoryDTO;
import com.codegym.model.Category;
import com.codegym.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    // Lấy tất cả phòng ban
    public List<CategoryDTO> getAllCatgory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> list = new ArrayList<>();
        for (Category category : categories) {
            CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
            list.add(dto);
        }
        return list;
    }

    // Lấy phòng ban theo id
    public CategoryDTO getCategoryById(Long id) {
        Optional<Category> department = categoryRepository.findById(id);
        if (department.isPresent()) {
            return new CategoryDTO(department.get().getId(), department.get().getName());
        }
        return null;
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
