package com.jobhunter.jobhunter.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomError {
    String statusCode;
    String message;

    // Empty constructor required by Lombok @Builder
    public CustomError() {
    }

    // Constructor to initialize statusCode and message
    public CustomError(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
