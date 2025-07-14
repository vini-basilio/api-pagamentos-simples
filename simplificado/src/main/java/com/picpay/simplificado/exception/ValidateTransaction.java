package com.picpay.simplificado.exception;

public class ValidateTransaction extends RuntimeException {
    public ValidateTransaction(String message) {
        super(message);
    }
}
