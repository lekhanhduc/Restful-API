package com.jobhunter.jobhunter.utils;

import com.jobhunter.jobhunter.dto.response.CompanyDTO;
import com.jobhunter.jobhunter.dto.response.LoginDTOResponse;
import com.jobhunter.jobhunter.dto.request.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {

    private final SecurityUtils securityUtils;

    public static User toUser(UserDTOCreate userDTOCreate){
        return User.builder()
                                .username(userDTOCreate.getUsername())
                                .email(userDTOCreate.getEmail())
                                .password(userDTOCreate.getPassword())
                                .age(userDTOCreate.getAge())
                                .address(userDTOCreate.getAddress())
                                .gender(userDTOCreate.getGender())
                                .build();
    }

    public static UserDTOResponse toUserDTOResponse(User user) {
        CompanyDTO companyDTO = null;
        if (user.getCompany() != null) {
            Company company = user.getCompany();
            companyDTO = CompanyDTO.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .build();
        }

        return UserDTOResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .address(user.getAddress())
                .age(user.getAge())
                .gender(user.getGender())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .company(companyDTO)
                .build();
    }

    public static LoginDTOResponse mapToDTOResponse(User user) {
        return LoginDTOResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .success(true)
                .build();
    }
}
