package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.pagination.Meta;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.response.JobDTOResponse;
import com.jobhunter.jobhunter.dto.response.JobDTOUpdate;
import com.jobhunter.jobhunter.entity.Job;
import com.jobhunter.jobhunter.entity.Skill;
import com.jobhunter.jobhunter.mapper.JobMapper;
import com.jobhunter.jobhunter.model.NotfoundException;
import com.jobhunter.jobhunter.repository.JobRepository;
import com.jobhunter.jobhunter.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public Job findById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(()->new NotfoundException("Job not found"));
    }

    public Job findJobByName(String name){
        return jobRepository.findByName(name)
                .orElseThrow(() -> new NotfoundException("Job not found"));
    }

   public void delete(Long id){
        Job jobId = jobRepository.findById(id)
                .orElseThrow(()->new NotfoundException("Job not found"));

        if(jobId != null){
            jobRepository.delete(jobId);
        }
   }

   public JobDTOResponse save(Job job){
        if(job.getSkills() != null){
            List<Long> skillCurrent = job.getSkills().stream().map(Skill::getId).toList();
            List<Skill> dbSkill = skillRepository.findAllById(skillCurrent);
            job.setSkills(dbSkill);
        }
        Job createJob = jobRepository.save(job);

        return JobMapper.toJobCreate(createJob);
   }

    public JobDTOUpdate update(Job job) {
        jobRepository.findById(job.getId())
                .orElseThrow(() -> new NotfoundException("Job not found"));

        if (job.getSkills() != null) {
            List<Long> skillCurrent = job.getSkills().stream().map(Skill::getId).toList();
            List<Skill> dbSkill = skillRepository.findAllById(skillCurrent);
            job.setSkills(dbSkill);
        }
        Job updatedJob = jobRepository.save(job);
        return JobMapper.toJobUpdate(updatedJob);
    }




    public ResultPaginationDTO findAll(Specification<Job> specification, Pageable pageable){
        Page<Job> listJob = jobRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setTotalPages(listJob.getTotalPages());
        meta.setTotal(listJob.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(listJob.getContent());

        return rs;
    }

}
