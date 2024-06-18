package com.jobhunter.jobhunter.utils;

import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.entity.Company;

public class CompanyMapper {

    private CompanyMapper(){
    }

    public static Company toCompany(CompanyDTOCreate companyDTOCreate){
        return Company.builder()
                .name(companyDTOCreate.getName())
                .address(companyDTOCreate.getAddress())
                .description(companyDTOCreate.getDescription())
                .logo(companyDTOCreate.getLogo())
                .build();
    }

    public static CompanyDTOResponse companyDTOResponse(Company company){
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
