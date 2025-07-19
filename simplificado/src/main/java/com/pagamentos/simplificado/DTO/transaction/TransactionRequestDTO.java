package com.pagamentos.simplificado.DTO.transaction;

import jakarta.validation.constraints.Min;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionRequestDTO(
        @Min(0) BigDecimal value,
        UUID payer,
        UUID payee
) { }
