package com.jobhunter.jobhunter.dto.response;


import com.jobhunter.jobhunter.utils.enums.Level;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.Instant;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobDTOResponse {

    Long id;
    String name;
    String location;
    double salary;
    int quantity;
    String description;
    Level level;
    List<String> skills;
    boolean active;
    Instant startDate;
    Instant endDate;
    Instant createdAt;
    String createdBy;
}
