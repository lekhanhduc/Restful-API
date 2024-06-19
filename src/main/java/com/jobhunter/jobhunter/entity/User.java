package com.jobhunter.jobhunter.entity;


import com.jobhunter.jobhunter.utils.SecurityUtils;
import com.jobhunter.jobhunter.utils.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(unique = true)
    @NotBlank(message = "Email cannot be left blank")
    String email;

    String username;

    @NotBlank(message = "Password cannot be left blank")
    String password;
    int age;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String address;
    String refreshToken;
    Instant createAt;
    Instant updateAt;
    String createBy;
    String updateBy;

    @PrePersist
    public void handlerBeforeCreate(){
        this.createBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        this.createAt = Instant.now();
    }

    @PreUpdate
    public void handlerAfterUpdate(){
        this.updateBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        this.updateAt = Instant.now();
    }
}
