package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.request.LoginDTORequest;
import com.jobhunter.jobhunter.dto.request.LoginDTOResponse;
import com.jobhunter.jobhunter.dto.response.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.exception.GlobalExceptionHandler;
import com.jobhunter.jobhunter.repository.UserRepository;
import com.jobhunter.jobhunter.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDTOResponse hanleSaveUser(UserDTOCreate userDTOCreate){
        // Buoc 1: chuyen DTO sang Entity
        User user = UserMapper.toUser(userDTOCreate);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        // Buoc 2: chuyen tu entity sang DTO de gui sang controller
        return UserMapper.toUserDTOResponse(user);
    }


    public void deleteUserById(Long id){
         userRepository.deleteById(id);
    }


    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }


    public UserDTOResponse updateUser(User request){
        User user = this.getUserById(request.getId());
        if(user == null){
            throw new RuntimeException("User not found");
        }
       return UserDTOResponse.builder().username(request.getUsername())
               .email(request.getEmail())
               .build();
    }

    public List<UserDTOResponse> getAll(){
        List<User> listUser = userRepository.findAll();

        List<UserDTOResponse> listUserDTOResponse = new ArrayList<>();
        for(User user : listUser){
            listUserDTOResponse.add(UserMapper.toUserDTOResponse(user));
        }
        return listUserDTOResponse;
    }

    public LoginDTOResponse login(LoginDTORequest request){
        User user = userRepository.findByEmail(request.getUsername());

        if(passwordEncoder.matches(request.getPassword(), user.getPassword())){
            return LoginDTOResponse.builder()
                    .success(true)
                    .build();
        }
        throw new GlobalExceptionHandler.IdInvalidException("Username or Password in correct");
        }
    }
