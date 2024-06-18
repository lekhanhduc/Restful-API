package com.jobhunter.jobhunter.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotfoundException extends RuntimeException{
    protected CustomError customError;

    public NotfoundException(CustomError customError) {
        this.customError = customError;
    }
}
