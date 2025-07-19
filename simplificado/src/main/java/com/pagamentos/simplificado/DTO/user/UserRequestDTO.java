package com.pagamentos.simplificado.DTO.user;

import com.pagamentos.simplificado.domain.user.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record UserRequestDTO(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String document,
        @Min(0) BigDecimal balance,
        @NotBlank @Email String email,
        @NotBlank String password,
        UserType userType) { }
