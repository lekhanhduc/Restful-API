package com.jobhunter.jobhunter.exception;

import com.jobhunter.jobhunter.model.CustomError;
import com.jobhunter.jobhunter.model.NotfoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler extends Exception {

    @ExceptionHandler(value = NotfoundException.class)
    public static ResponseEntity<CustomError> handleNotFoundException(NotfoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getCustomError());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public static ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }



    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid User ID")
    public static class IdInvalidException extends RuntimeException {
        public IdInvalidException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public static ResponseEntity<?> handleValidationCompanyExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error ->{
            String errorMessage = error.getDefaultMessage();
            errors.put("message", errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
