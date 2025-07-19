package com.pagamentos.simplificado.infra;

import com.pagamentos.simplificado.DTO.ExceptionDTO;
import com.pagamentos.simplificado.exception.UserCreationException;
import com.pagamentos.simplificado.exception.ValidateTransactionException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ExceptionDTO>> handleValidationErrors(MethodArgumentNotValidException ex) {

        var errorListResponse = new ArrayList<ExceptionDTO>();

        for (var error: ex.getFieldErrors()) {
            var fieldName = error.getField();
            var fieldErrorMessage = ex.getFieldError(fieldName).getDefaultMessage();
            var exceptionMessage = String.format("O campo '%s': %s", fieldName, fieldErrorMessage);
            errorListResponse.add(new ExceptionDTO(exceptionMessage));
        }

        return ResponseEntity.badRequest().body(errorListResponse);
    }
}