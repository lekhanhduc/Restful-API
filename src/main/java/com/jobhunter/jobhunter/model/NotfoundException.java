package com.jobhunter.jobhunter.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotfoundException extends RuntimeException {
    private CustomError customError;

    public NotfoundException(String message) {
        super(message);
        this.customError = new CustomError("404", message); 
    }
}