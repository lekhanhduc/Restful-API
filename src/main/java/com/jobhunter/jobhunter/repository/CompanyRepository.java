package com.jobhunter.jobhunter.repository;

import com.jobhunter.jobhunter.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Company findCompanyByName(String name);
}
