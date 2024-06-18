package com.jobhunter.jobhunter.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDTORequest {

    @NotBlank(message = "username not be empty")
    String username;

    @NotBlank(message = "password not be empty")
    String password;

}
