package com.jobhunter.jobhunter.dto.pagination;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meta {
    int page;
    int pageSize;
    int totalPages;
    long total;
}
