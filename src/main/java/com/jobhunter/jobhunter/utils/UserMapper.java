package com.jobhunter.jobhunter.utils;

import com.jobhunter.jobhunter.dto.response.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.User;

public class UserMapper {

    private UserMapper(){
    }

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
}
