package com.jobhunter.jobhunter.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jobhunter.jobhunter.utils.SecurityUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;


@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty(message = "name không được để trống")
    String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    String address;
    String logo;


    Instant createAt;
    Instant updateAt;

    String createBy;
    String updateBy;

    @PrePersist
    public void handlerBeforeCreate(){
        this.createBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        this.createAt = Instant.now();
    }
}
