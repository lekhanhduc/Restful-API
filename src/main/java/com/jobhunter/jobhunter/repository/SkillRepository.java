package com.jobhunter.jobhunter.repository;


import com.jobhunter.jobhunter.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> , JpaSpecificationExecutor<Skill> {

    Skill findSkillByName(String name);
}
