package com.jobhunter.jobhunter.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("access_token")
    String token;
}
