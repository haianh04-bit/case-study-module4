package com.codegym.service;

import com.codegym.dto.UserDTO;
import com.codegym.model.User;
import com.codegym.model.VerificationToken;
import com.codegym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final String UPLOAD_DIR = "D:/module4/case-study/uploads/user/";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileUploadService fileUploadService;

    // Mapping entity -> DTO
    private UserDTO mapToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress(),
                user.getImageUrl()
        );
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Tìm user theo email (Spring Security sẽ dùng)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    // Cập nhật profile
    public void updateUser(Long id, UserDTO dto, MultipartFile avatarFile) throws IOException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Xoá avatar cũ nếu có
            if (user.getImageUrl() != null) {
                fileUploadService.deleteFile(UPLOAD_DIR + "/" + user.getImageUrl());
            }
            // Upload avatar mới
            String fileName = fileUploadService.uploadFile(UPLOAD_DIR, avatarFile);
            user.setImageUrl(fileName);
        }

        userRepository.save(user);
    }

    // Xoá user (admin)
    public void deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getImageUrl() != null) {
                fileUploadService.deleteFile(UPLOAD_DIR + "/" + user.getImageUrl());
            }
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // dùng email làm username trong Spring Security
                .password(user.getPassword()) // password đã mã hoá BCrypt trong DB
                .roles(user.getRole().name().replace("ROLE_", "")) // ví dụ USER
                .disabled(!user.isEnabled())
                .build();
    }

}
