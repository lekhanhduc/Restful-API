package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.annotation.ApiMessage;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.request.CompanyDTOUpdate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.dto.response.CompanyDTOUpdateResponse;
import com.jobhunter.jobhunter.dto.response.DeleteDTOResponse;
import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.service.CompanyService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyDTOCreate companyDTOCreate){

        CompanyDTOResponse companyDTOResponse = companyService.saveCompany(companyDTOCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyDTOResponse);
    }

    @GetMapping("/companies")
    @ApiMessage("Get All Company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(//@RequestParam("current") Optional<String> currentOptional,
                                                             //@RequestParam("pageSize") Optional<String> pageSizeOptional){
                                                             @Filter Specification<Company> spec,
                                                             Pageable pageable){

//        String sCurrent = currentOptional.isPresent() ? currentOptional.get() : "";
//        String sPageSize = pageSizeOptional.isPresent() ? pageSizeOptional.get() : "";
//
//        int current = Integer.parseInt(sCurrent);
//        int pageSize = Integer.parseInt(sPageSize);
//
//        Pageable pageable = PageRequest.of(current - 1, pageSize);

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompany(spec, pageable));
    }

    @GetMapping("companies/{id}")
    public ResponseEntity<CompanyDTOResponse> getCompanyById(@PathVariable Long id){
        return ResponseEntity.ok().body(companyService.getById(id));
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<CompanyDTOUpdateResponse> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyDTOUpdate companyDTOUpdate) {
        CompanyDTOUpdateResponse companyDTOResponse = companyService.updateCompany(id, companyDTOUpdate);
        return ResponseEntity.ok().body(companyDTOResponse);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<DeleteDTOResponse> deleteCompany(@PathVariable Long id){
        return ResponseEntity.ok().body(companyService.deleteCompany(id));
    }
}
