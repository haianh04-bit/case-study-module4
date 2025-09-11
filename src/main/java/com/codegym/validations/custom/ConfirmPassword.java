package com.codegym.validations.custom;

import com.codegym.validations.PasswordMatchesValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface ConfirmPassword {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends javax.validation.Payload>[] payload() default {};

}
