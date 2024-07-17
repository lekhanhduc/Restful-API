package com.jobhunter.jobhunter.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jobhunter.jobhunter.utils.SecurityUtils;
import com.jobhunter.jobhunter.utils.enums.Level;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String location;
    double salary;
    int quantity;
    @Column(columnDefinition = "MEDIUMTEXT")
    String description;

    @Enumerated(EnumType.STRING)
    Level level;

    Instant startDate;
    Instant endDate;
    boolean active;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;

    @ManyToOne()
    @JoinColumn(name = "company_id")
    Company company;

    @ManyToMany(fetch = FetchType.LAZY)
//    @JsonIgnore: giả sử dùng cái này thì khi ở frontend truyền lên 1 list<Skill> thì ở backend sẽ không nhận được
    @JsonIgnoreProperties(value = {"jobs"}) // chỉ bỏ đi jobs khi gọi skill, tức là khi mình gửi skill thì jobs sẽ không được gửi kèm
    @JoinTable(name = "job_skill", joinColumns = @JoinColumn(name = "job_id"),
                                    inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;

    @PrePersist
    public void handlerBeforeCreate(){
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
    }
    @PreUpdate
    public void handlerAfterUpdate(){
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
    }
}
