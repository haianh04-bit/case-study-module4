package com.codegym.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private String categoryName;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
