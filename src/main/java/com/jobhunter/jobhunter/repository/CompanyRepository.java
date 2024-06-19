package com.jobhunter.jobhunter.repository;

import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {

    Company findCompanyByName(String name);
}
