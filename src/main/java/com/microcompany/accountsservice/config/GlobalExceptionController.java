package com.microcompany.accountsservice.config;

import com.microcompany.accountsservice.exception.AccountNotFoundException;
import com.microcompany.accountsservice.exception.CustomerNotFoundException;
import com.microcompany.accountsservice.exception.GlobalException;
import com.microcompany.accountsservice.model.StatusMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;


@RestController
public class GlobalExceptionController {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<StatusMessage> handleAccountNotFoundException(GlobalException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusMessage(HttpStatus.NOT_FOUND.value(), "GE: No encontrado"));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StatusMessage handleNewProductException(AccountNotFoundException ex) {
        return new StatusMessage(HttpStatus.NOT_FOUND.value(), "Cuenta no encontrada");
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public StatusMessage handleNewProductException(CustomerNotFoundException ex) {
        return new StatusMessage(HttpStatus.NOT_FOUND.value(), "Cliente no encontrado");
    }


    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public StatusMessage handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new StatusMessage(HttpStatus.PRECONDITION_FAILED.value(), "El argumento introducido no es valido");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.PRECONDITION_FAILED);
    }
}
