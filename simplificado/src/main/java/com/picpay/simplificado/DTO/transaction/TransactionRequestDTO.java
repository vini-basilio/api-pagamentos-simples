package com.picpay.simplificado.DTO.transaction;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(
        BigDecimal value,
        UUID senderId,
        UUID receiverId
) { }
