package com.picpay.simplificado.DTO;
import java.math.BigDecimal;
public record UserCreatedDTO(
        String firstName,
        String lastName,
        BigDecimal balance,
        Long id
){}