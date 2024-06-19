package com.jobhunter.jobhunter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailAlreadyExistsException {
    private int statusCode;
    private String error;
    private String message;

}
