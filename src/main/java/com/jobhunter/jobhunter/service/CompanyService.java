package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.exception.GlobalExceptionHandler;
import com.jobhunter.jobhunter.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDTOResponse saveCompany(CompanyDTOCreate companyDTOCreate){

        String name = companyDTOCreate.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name Company không được để trống");
        }

        Company cpn = companyRepository.findCompanyByName(name);
        if(cpn != null){
            throw new IllegalArgumentException("Name Company đã tồn tại");
        }

        // Chuyển dto request sang entity
        Company company = Company.builder()
                .name(companyDTOCreate.getName())
                .address(companyDTOCreate.getAddress())
                .description(companyDTOCreate.getDescription())
                .logo(companyDTOCreate.getLogo())

                .build();

        companyRepository.save(company);

        // Chuyển từ entity sang dto response
        return CompanyDTOResponse.builder()
                .name(company.getName())
                .address(company.getAddress())
                .description(company.getDescription())
                .logo(company.getLogo())
                .createBy(company.getCreateBy())
                .createAt(company.getCreateAt())
                .success(true)
                .build();
    }
}
