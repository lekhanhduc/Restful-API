package com.jobhunter.jobhunter.service;

import com.jobhunter.jobhunter.dto.pagination.Meta;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.request.CompanyDTOUpdate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.dto.response.CompanyDTOUpdateResponse;
import com.jobhunter.jobhunter.dto.response.DeleteDTOResponse;
import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.model.ResourceNotFoundException;
import com.jobhunter.jobhunter.repository.CompanyRepository;
import com.jobhunter.jobhunter.utils.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public ResultPaginationDTO fetchAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta mt = new Meta();

        mt.setPage(pageable.getPageNumber() + 1); // từ frontend truyền lên, trang hiện tại
        mt.setPageSize(pageable.getPageSize());  // từ frontend truyền lên, số lượng phần tử của trang

        mt.setTotalPages(companyPage.getTotalPages()); // truy vấn từ database, để Thiết lập tổng số trang
        mt.setTotal(companyPage.getTotalElements());  // truy vấn lấy từ database để thiết lập tổng số trang, và tổng số phần tử

        rs.setMeta(mt);
        rs.setResult(companyPage.getContent());

        return rs;
    }

    public CompanyDTOResponse getById(Long id) {
        return companyRepository.findById(id)
                .map(CompanyMapper::companyDTOResponse)
                .orElseThrow(() ->  new ResourceNotFoundException("User not found with id " + id));
    }

    public CompanyDTOUpdateResponse updateCompany(Long id, CompanyDTOUpdate companyDTOUpdate) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() ->   new ResourceNotFoundException("User not found with id " + id));

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

    public DeleteDTOResponse deleteCompany(Long id){
        Company company = companyRepository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("User not found with id " + id));

        companyRepository.deleteById(company.getId());
        return DeleteDTOResponse.builder()
                .message("Delete Successfully")
                .success(true)
                .build();
    }

}