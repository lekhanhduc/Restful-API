package com.jobhunter.jobhunter.utils;

import com.jobhunter.jobhunter.dto.request.LoginDTOResponse;
import com.jobhunter.jobhunter.dto.response.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserMapper {

    private final SecurityUtils securityUtils;

    public static User toUser(UserDTOCreate userDTOCreate){
        return User.builder()
                                .username(userDTOCreate.getName())
                                .email(userDTOCreate.getEmail())
                                .password(userDTOCreate.getPassword())
                                .age(userDTOCreate.getAge())
                                .address(userDTOCreate.getAddress())
                                .gender(userDTOCreate.getGender())
                                .build();
    }

    public static UserDTOResponse toUserDTOResponse(User user){
        return UserDTOResponse.builder()
                                         .email(user.getEmail())
                                         .username(user.getUsername())
                                         .address(user.getAddress())
                                         .age(user.getAge())
                                         .gender(user.getGender())
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
