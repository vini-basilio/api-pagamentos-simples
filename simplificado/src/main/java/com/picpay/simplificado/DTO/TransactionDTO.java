package com.picpay.simplificado.DTO;

import java.math.BigDecimal;

public record TransactionDTO(
        BigDecimal value,
        Long senderId,
        Long receiverId
) { }
