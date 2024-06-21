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
    @Column(columnDefinition = "MEDIUMTEXT")
    String refreshToken;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    Company company;

    @PrePersist
    public void handlerBeforeCreate(){
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handlerAfterUpdate(){
        this.updatedBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        this.updatedAt = Instant.now();
    }
}
