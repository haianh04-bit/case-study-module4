package com.codegym.validations;

import com.codegym.dto.RegisterDTO;
import com.codegym.validations.custom.ConfirmPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<ConfirmPassword, RegisterDTO> {
    @Override
    public boolean isValid(RegisterDTO registerDTO, ConstraintValidatorContext context) {
        if (registerDTO.getPassword() == null || registerDTO.getConfirmPassword() == null) {
            return false;
        }
        return registerDTO.getPassword().equals(registerDTO.getConfirmPassword());
    }
}
