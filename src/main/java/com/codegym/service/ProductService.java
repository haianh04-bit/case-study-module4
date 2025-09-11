package com.codegym.service;

import com.codegym.dto.CreateProductDTO;
import com.codegym.dto.ProductDTO;
import com.codegym.model.Category;
import com.codegym.model.Product;
import com.codegym.repository.CategoryRepository;
import com.codegym.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ProductService {
    private final String UPLOAD_DIR = "D:/module4/case-study/uploads/product/";
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FileUploadService fileUploadService;

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
        return dto;
    }

    public Page<ProductDTO> findAll(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToDTO);
    }

    public Page<ProductDTO> findByCategory(Category category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable).map(this::mapToDTO);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product findCartId(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Optional<ProductDTO> findByIdDB(Long id) {
        return productRepository.findById(id).map(this::mapToDTO);
    }

    public void createProduct(CreateProductDTO dto) throws IOException {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        MultipartFile file = dto.getImage();
        if (file != null && !file.isEmpty()) {
            String fileName = fileUploadService.uploadFile(UPLOAD_DIR, file);
            product.setImageUrl(fileName);
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        productRepository.save(product);
    }


    public void deleteById(int id) {
        Optional<Product> product = productRepository.findById((long) id);
        if (product.isPresent()) {
            Product currentProduct = product.get();
            fileUploadService.deleteFile(UPLOAD_DIR + "/" + currentProduct.getImageUrl());
            productRepository.delete(currentProduct);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    public void updateProduct(Long id, CreateProductDTO dto) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());

        MultipartFile file = dto.getImage();
        if (file != null && !file.isEmpty()) {
            if (product.getImageUrl() != null) {
                fileUploadService.deleteFile(UPLOAD_DIR + "/" + product.getImageUrl());
            }
            String fileName = fileUploadService.uploadFile(UPLOAD_DIR, file);
            product.setImageUrl(fileName);
        }
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        productRepository.save(product);
    }

}
