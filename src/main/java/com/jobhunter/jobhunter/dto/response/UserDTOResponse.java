package com.jobhunter.jobhunter.dto.response;


import com.jobhunter.jobhunter.utils.enums.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserDTOResponse {
    String email;
    String username;
    String address;
    int age;
    Gender gender;
}
