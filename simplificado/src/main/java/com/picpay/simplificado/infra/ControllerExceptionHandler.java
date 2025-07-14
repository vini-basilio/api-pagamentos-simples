package com.picpay.simplificado.infra;

import com.picpay.simplificado.DTO.ExceptionDTO;
import com.picpay.simplificado.exception.UserCreationException;
import com.picpay.simplificado.exception.ValidateTransactionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ValidateTransactionException.class)
    public ResponseEntity<ExceptionDTO> handleInvalidateTransaction(ValidateTransactionException ex) {
        ExceptionDTO errorResponse = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(ex.getCode()).body(errorResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleNotFoundUser(DataIntegrityViolationException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserCreationException.class)
    public ResponseEntity<ExceptionDTO> handleDuplicateEntry(UserCreationException ex) {
        ExceptionDTO errorResponse = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleGenericError(DataIntegrityViolationException ex){
        ExceptionDTO errorResponse = new ExceptionDTO(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}