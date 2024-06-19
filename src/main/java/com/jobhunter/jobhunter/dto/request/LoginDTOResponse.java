package com.jobhunter.jobhunter.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDTOResponse {
    Long id;
    String email;
    String username;
    boolean success;
    String token;
}
