package com.codegym.dto;

import com.codegym.validations.custom.Image;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
public class ProductDTO {

    private Long id;
    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(min = 3, max = 100, message = "Tên sản phẩm phải từ 3 - 100 ký tự")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.01", message = "Giá phải lớn hơn 0")
    private Double price;

    @Image
    private String imageUrl;

    @NotBlank(message = "Danh mục không được để trống")
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
