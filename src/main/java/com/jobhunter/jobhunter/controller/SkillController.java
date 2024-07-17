package com.jobhunter.jobhunter.controller;

import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.entity.Skill;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.service.SkillService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/skill")
public class SkillController {

    private final SkillService skillService;

    @PostMapping("/save")
    public ResponseEntity<Skill> saveSkill(@RequestBody Skill skill){
        skillService.save(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(Skill
                .builder()
                        .name(skill.getName())
                        .createdBy(skill.getCreatedBy())
                        .createdAt(skill.getCreatedAt())
                .build());
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateSkill(@RequestBody Skill skill) {
        Skill updatedSkill = skillService.update(skill);
        return ResponseEntity.ok().body(updatedSkill);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(@Filter Specification<Skill> spec, Pageable pageable){
        return ResponseEntity.ok().body(skillService.getAll(spec, pageable));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSkillById(@PathVariable Long id) {
        skillService.deleteById(id);
        return ResponseEntity.ok().body("Skill deleted successfully");
    }
}
