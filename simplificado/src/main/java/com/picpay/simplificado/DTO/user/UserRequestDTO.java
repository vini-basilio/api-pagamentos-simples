package com.picpay.simplificado.DTO.user;

import com.picpay.simplificado.domain.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UserRequestDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String document,
        BigDecimal balance,
        @NotBlank @Email String email,
        @NotBlank String password,
        UserType userType) { }
