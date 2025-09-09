package com.codegym.validations.custom;

import com.codegym.validations.PasswordMatchesValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface ConfirmPassword {
    String message() default "Mật khẩu xác nhận không khớp!";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};
}
