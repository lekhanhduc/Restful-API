package com.jobhunter.jobhunter.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyDTOUpdate {

    String name;
    String description;
    String address;
    String logo;
}
