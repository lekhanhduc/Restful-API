package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.request.CompanyDTOUpdate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.exception.GlobalExceptionHandler;
import com.jobhunter.jobhunter.model.ResourceNotFoundException;
import com.jobhunter.jobhunter.repository.CompanyRepository;
import com.jobhunter.jobhunter.utils.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDTOResponse saveCompany(CompanyDTOCreate companyDTOCreate) {

        String name = companyDTOCreate.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name Company không được để trống");
        }
        Company cpn = companyRepository.findCompanyByName(name);
        if (cpn != null) {
            throw new IllegalArgumentException("Name Company đã tồn tại");
        }
        Company company = CompanyMapper.toCompany(companyDTOCreate);
        companyRepository.save(company);
        return CompanyMapper.companyDTOResponse(company);
    }

    public List<CompanyDTOResponse> getAllCompany() {
        List<Company> listCompany = companyRepository.findAll();
        return CompanyMapper.companyDTOResponseList(listCompany);
    }

    public CompanyDTOResponse getById(Long id) {

        return companyRepository.findById(id)
                .map(CompanyMapper::companyDTOResponse)
                .orElseThrow(() -> new GlobalExceptionHandler.IdInvalidException("Company not found with id:" + id));
    }


    public CompanyDTOResponse updateCompany(Long id, CompanyDTOUpdate companyDTOUpdate) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.IdInvalidException("Company not exists"));


        if (companyDTOUpdate.getName() != null) {
            if (!StringUtils.hasText(companyDTOUpdate.getName())) {
                throw new IllegalArgumentException("Name Company không được để trống");
            }
            existingCompany.setName(companyDTOUpdate.getName());
        }
        if (companyDTOUpdate.getAddress() != null) {
            existingCompany.setAddress(companyDTOUpdate.getAddress());
        }
        if (companyDTOUpdate.getDescription() != null) {
            existingCompany.setDescription(companyDTOUpdate.getDescription());
        }
        if (companyDTOUpdate.getLogo() != null) {
            existingCompany.setLogo(companyDTOUpdate.getLogo());
        }

        Company updatedCompany = companyRepository.save(existingCompany);

        return CompanyMapper.companyDTOResponseUpdate(updatedCompany);
    }

}
