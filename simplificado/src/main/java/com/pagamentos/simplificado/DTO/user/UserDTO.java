package com.pagamentos.simplificado.DTO.user;
import com.pagamentos.simplificado.domain.user.UserType;

import java.math.BigDecimal;
import java.util.UUID;

public record UserDTO(
        UUID id,
        String firstName,
        String lastName,
        BigDecimal balance,
        String email,
        UserType userType
){}