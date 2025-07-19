package com.pagamentos.simplificado.DTO.transaction;

import com.pagamentos.simplificado.DTO.user.UserDTO;

import java.util.UUID;

public record TransactionDTO(
        UUID id,
        UserDTO payer,
        UserDTO payee
) { }
