package com.picpay.simplificado.DTO.transaction;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        BigDecimal value,
        Long senderId,
        Long receiverId
) { }
