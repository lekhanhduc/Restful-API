package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.pagination.Meta;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.request.LoginDTORequest;
import com.jobhunter.jobhunter.dto.request.LoginDTOResponse;
import com.jobhunter.jobhunter.dto.response.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.exception.GlobalExceptionHandler;
import com.jobhunter.jobhunter.model.ResourceNotFoundException;
import com.jobhunter.jobhunter.repository.UserRepository;
import com.jobhunter.jobhunter.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserDTOResponse hanleSaveUser(UserDTOCreate userDTOCreate){
        User findUserByEmail = userRepository.findByEmail(userDTOCreate.getEmail());
        if(findUserByEmail != null){
            throw new DataIntegrityViolationException("Email already exists: " + userDTOCreate.getEmail());
        }
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
            throw new ResourceNotFoundException("Id User not found");
        }
       return UserMapper.toUserDTOResponse(request);
    }

    public ResultPaginationDTO getAll(Specification<User> specification, Pageable pageable){
        Page<User> listUser = userRepository.findAll(specification, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());

        meta.setTotalPages(listUser.getTotalPages());
        meta.setTotal(listUser.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(listUser.getContent());

        return rs;
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

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUserToken(String token, String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setRefreshToken(token);
            userRepository.save(user);
        }
    }
}
