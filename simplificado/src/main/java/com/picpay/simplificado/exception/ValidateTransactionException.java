package com.picpay.simplificado.exception;

public class ValidateTransactionException extends RuntimeException {
    private int code;
    public ValidateTransactionException(String message, int code) {
        super(message);
        this.code = code;
    }
    public int getCode() { return code; }
}
