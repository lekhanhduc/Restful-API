package com.jobhunter.jobhunter.dto.response;


import com.jobhunter.jobhunter.utils.enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserDTOCreate {
    String email;
    String name;
    String password;
    String address;
    int age;
    @Enumerated(EnumType.STRING)
    Gender gender;
}
