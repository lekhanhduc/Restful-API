package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.pagination.Meta;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.entity.Skill;
import com.jobhunter.jobhunter.model.NotfoundException;
import com.jobhunter.jobhunter.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    public Skill findById(Long id){
        return skillRepository.findById(id)
                .orElseThrow(()-> new NotfoundException("SKill not exists"));
    }

    public ResultPaginationDTO getAll(Specification<Skill> specification, Pageable pageable){
        Page<Skill> listSkill = skillRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setTotalPages(listSkill.getTotalPages());
        meta.setTotal(listSkill.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(listSkill.getContent());

        return rs;
    }

    public void save(Skill skill){
        Skill findSkill = skillRepository.findSkillByName(skill.getName());
        if(findSkill == null){
            skillRepository.save(skill);
            System.out.println("Save Skill Success");
        } else {
            throw new BadCredentialsException("Skill " +skill.getName()+ " already exists");
        }
    }

    public Skill update(Skill skill) {
        Skill findSkill = skillRepository.findById(skill.getId()).orElseThrow(() -> new BadCredentialsException("Skill not Exists"));
        if (findSkill.getName().equals(skill.getName())) {
            throw new BadCredentialsException("Skill already Exists");
        }
        findSkill.setName(skill.getName());
        return skillRepository.save(findSkill);
    }

    public void deleteById(Long id){
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new NotfoundException("Skill not found"));
        if(skill != null){
            // khi xóa 1 skill(kĩ năng) -> thì phải xóa kĩ năng đó bên bảng con(bảng trung gian giũa job và skill)
            // vd: job a: có kĩ năng 1,2,3 => khi xóa kĩ năng 1 bên bảng skill => thì xóa luôn kĩ năng 1 lưu bên bảng job_skill
            // => job a chỉ còn kĩ năng 2,3
            skill.getJobs().forEach(job -> job.getSkills().remove(skill));
            skillRepository.delete(skill);
        }
    }
}
