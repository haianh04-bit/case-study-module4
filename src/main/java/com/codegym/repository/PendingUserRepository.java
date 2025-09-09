package com.codegym.repository;

import com.codegym.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByEmail(String email);
    Optional<VerificationToken> findByEmailAndOtp(String email, String otp);

    void deleteByEmail(String email);
}
