package com.codegym.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String address;
    private String imageUrl; // để hiển thị
}
