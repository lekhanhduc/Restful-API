package com.jobhunter.jobhunter.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobhunter.jobhunter.model.CustomError;
import com.jobhunter.jobhunter.model.NotfoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler extends Exception {


    private final ObjectMapper objectMapper = new ObjectMapper();


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

}
