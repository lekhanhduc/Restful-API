package com.jobhunter.jobhunter.controller;

import com.jobhunter.jobhunter.dto.request.LoginDTORequest;
import com.jobhunter.jobhunter.dto.request.LoginDTOResponse;
import com.jobhunter.jobhunter.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;

    @PostMapping("/login")
    public ResponseEntity<LoginDTOResponse> login(@Valid @RequestBody LoginDTORequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be null or empty");
        }

        // truyền input đầu vào là username and password
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        // Xác thực người dùng => viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // tạo token
        String access_token = securityUtils.createToke(authentication);

        return ResponseEntity.ok().body(LoginDTOResponse.builder()
                        .success(true)
                        .token(access_token)
                            .build());
    }

    @GetMapping("/")
    public String getHelloWord(){
        return "Hello Khanh Duc";
    }
}
