package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.dto.request.CompanyDTOCreate;
import com.jobhunter.jobhunter.dto.response.CompanyDTOResponse;
import com.jobhunter.jobhunter.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CompanyDTOResponse>> getAllCompany(){
        List<CompanyDTOResponse> listDtoResponses = companyService.getAllCompany();
        return ResponseEntity.ok().body(listDtoResponses);
    }

    @GetMapping("companies/{id}")
    public ResponseEntity<CompanyDTOResponse> getCompanyById(@PathVariable Long id){
        return ResponseEntity.ok().body(companyService.getById(id));
    }
}
