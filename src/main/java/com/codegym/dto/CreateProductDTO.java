package com.codegym.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class CreateProductDTO {
    private String name;
    private String description;
    private Double price;

    private MultipartFile image;

    private Long categoryId;


}
