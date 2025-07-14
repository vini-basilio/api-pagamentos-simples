package com.picpay.simplificado.DTO;

public record TransactionDTO(
        Long id,
        UserDTO sender,
        UserDTO receiver
) {
}
