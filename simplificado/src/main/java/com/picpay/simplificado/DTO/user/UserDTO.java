package com.picpay.simplificado.DTO.user;
import com.picpay.simplificado.domain.user.UserType;

import java.math.BigDecimal;
public record UserDTO(
        Long id,
        String firstName,
        String lastName,
        BigDecimal balance,
        String email,
        UserType userType
){}