package com.picpay.simplificado.DTO.transaction;

import com.picpay.simplificado.DTO.user.UserDTO;

import java.util.UUID;

public record TransactionDTO(
        UUID id,
        UserDTO payer,
        UserDTO payee
) { }
