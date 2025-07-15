package com.picpay.simplificado.DTO.transaction;

import com.picpay.simplificado.DTO.user.UserDTO;

public record TransactionDTO(
        Long id,
        UserDTO sender,
        UserDTO receiver
) {
}
