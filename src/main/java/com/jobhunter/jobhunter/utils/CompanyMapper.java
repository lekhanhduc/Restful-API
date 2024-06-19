package com.jobhunter.jobhunter.utils;

import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.dto.response.CompanyDTOUpdateResponse;
import com.jobhunter.jobhunter.entity.Company;

import java.util.List;

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

    public static CompanyDTOUpdateResponse companyDTOResponseUpdate(Company company){
        return CompanyDTOUpdateResponse.builder()
                .name(company.getName())
                .address(company.getAddress())
                .description(company.getDescription())
                .logo(company.getLogo())
                .updateBy(company.getUpdateBy())
                .updateAt(company.getUpdateAt())
                .success(true)
                .build();
    }


    public static List<CompanyDTOResponse> companyDTOResponseList(List<Company> companyList){
        return companyList.stream().map(t -> CompanyDTOResponse.builder()
                        .name(t.getName())
                        .address(t.getAddress())
                        .description(t.getDescription())
                        .createAt(t.getCreateAt())
                        .createBy(t.getCreateBy())
                        .build())
                        .toList();
    }
}
