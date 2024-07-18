package com.jobhunter.jobhunter.mapper;

import com.jobhunter.jobhunter.dto.response.JobDTOResponse;
import com.jobhunter.jobhunter.dto.response.JobDTOUpdate;
import com.jobhunter.jobhunter.entity.Job;
import com.jobhunter.jobhunter.entity.Skill;

import java.util.List;

public class JobMapper {

    private JobMapper(){
    }

    public static JobDTOResponse toJobCreate(Job job) {

        List<String> skills = null;
        if (job.getSkills() != null) {
            skills = job.getSkills().stream().map(Skill::getName).toList();
        }
        return JobDTOResponse.
                builder()
                .id(job.getId())
                .name(job.getName())
                .level(job.getLevel())
                .skills(skills)
                .location(job.getLocation())
                .salary(job.getSalary())
                .quantity(job.getQuantity())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .active(job.isActive())
                .createdAt(job.getCreatedAt())
                .createdBy(job.getCreatedBy())
                .build();
    }

    public static JobDTOUpdate toJobUpdate(Job job) {
        List<String> skills = null;
        if (job.getSkills() != null) {
            skills = job.getSkills().stream().map(Skill::getName).toList();
        }
        return JobDTOUpdate.
                builder()
                .id(job.getId())
                .name(job.getName())
                .level(job.getLevel())
                .skills(skills)
                .location(job.getLocation())
                .salary(job.getSalary())
                .quantity(job.getQuantity())
                .startDate(job.getStartDate())
                .endDate(job.getEndDate())
                .active(job.isActive())
                .updatedBy(job.getUpdatedBy())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
