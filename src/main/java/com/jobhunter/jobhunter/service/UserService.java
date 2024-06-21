package com.jobhunter.jobhunter.service;


import com.jobhunter.jobhunter.dto.pagination.Meta;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.request.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.DeleteDTOResponse;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.Company;
import com.jobhunter.jobhunter.entity.User;
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
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;

    public UserDTOResponse handleSaveUser(UserDTOCreate userDTOCreate) {
        if (userRepository.existsByEmail(userDTOCreate.getEmail())) {
            throw new DataIntegrityViolationException("Email already exists: " + userDTOCreate.getEmail());
        }

        User user = UserMapper.toUser(userDTOCreate);
        user.setPassword(passwordEncoder.encode(userDTOCreate.getPassword()));

        if (userDTOCreate.getCompanyId() != null) {
            Long companyId = userDTOCreate.getCompanyId();
            Company company = companyService.findById(companyId).orElse(null);
            user.setCompany(company);
        }

        user = userRepository.save(user);
        return UserMapper.toUserDTOResponse(user);
    }


    public DeleteDTOResponse deleteUserById(Long id){
         User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

         userRepository.deleteById(user.getId());
        return DeleteDTOResponse.builder()
                .message("Delete Successfully")
                .success(true)
                .build();
    }


    public UserDTOResponse getUserById(Long id) {
       return userRepository.findById(id).map(UserMapper::toUserDTOResponse)
               .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserDTOResponse updateUser(Long id, UserDTOCreate userDTOCreate) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->   new ResourceNotFoundException("User not found with id " + id));

        if (userDTOCreate.getEmail() != null) {
            if (!StringUtils.hasText(userDTOCreate.getUsername())) {
                throw new IllegalArgumentException("Name Company không được để trống");
            }
            existingUser.setEmail(userDTOCreate.getEmail());
        }
        if (userDTOCreate.getUsername() != null) {
            existingUser.setUsername(userDTOCreate.getUsername());
        }
        if (userDTOCreate.getAge() != 0) {
            existingUser.setAge(userDTOCreate.getAge());
        }
        if (userDTOCreate.getAddress() != null) {
            existingUser.setAddress(userDTOCreate.getAddress());
        }

        if(userDTOCreate.getCompanyId() != null){
            Company company = companyService.findById(userDTOCreate.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company not found with id " + userDTOCreate.getCompanyId()));
            existingUser.setCompany(company);
        }

        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toUserDTOResponse(updatedUser);
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

    public User findByRefreshTokenAndEmail(String token, String email){
        return userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
