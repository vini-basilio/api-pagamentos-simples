package com.picpay.simplificado.infra;

import com.picpay.simplificado.DTO.ExceptionDTO;
import com.picpay.simplificado.exception.UserCreation;
import com.picpay.simplificado.exception.ValidateTransaction;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ValidateTransaction.class)
    public ResponseEntity<ExceptionDTO> threatInvalidateTransaction(ValidateTransaction ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDTO(ex.getMessage(), "500"));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threatNotFoundUser(DataIntegrityViolationException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserCreation.class)
    public ResponseEntity<ExceptionDTO> threatDuplicateEntry(UserCreation ex) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionDTO(ex.getMessage(), "400"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGenericError(DataIntegrityViolationException ex){
        return ResponseEntity
                .internalServerError()
                .body(new ExceptionDTO(ex.getMessage(), "500"));
    }
}