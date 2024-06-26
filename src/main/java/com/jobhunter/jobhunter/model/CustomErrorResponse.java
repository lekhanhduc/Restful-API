package com.jobhunter.jobhunter.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomErrorResponse {
    String message;
    Date timestamp;
    int status;
    String error;
}