package com.jobhunter.jobhunter.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponse<T> {
    private int statusCode;
    private T data;
    private String message;
}
