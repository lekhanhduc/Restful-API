package com.jobhunter.jobhunter.repository;

import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User findByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

    boolean existsByEmail(String email);

    List<User> findByCompany(Company company);
}
