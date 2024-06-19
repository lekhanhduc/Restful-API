package com.jobhunter.jobhunter.dto.pagination;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResultPaginationDTO {

    Meta meta;
    Object result;

}
