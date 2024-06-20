package com.jobhunter.jobhunter.controller;

import com.jobhunter.jobhunter.annotation.ApiMessage;
import com.jobhunter.jobhunter.dto.request.LoginDTORequest;
import com.jobhunter.jobhunter.dto.request.LoginDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.service.UserService;
import com.jobhunter.jobhunter.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long REFRESH_EXPIRATION;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    @PostMapping("/login")
    // API này mục đích giúp frontend lấy được access_Token --> lấy token từ phía sever về frontend
    public ResponseEntity<LoginDTOResponse> login(@Valid @RequestBody LoginDTORequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be null or empty");
        }

        // Xác thực người dùng
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Lấy thông tin người dùng hiện tại
        User currentUser = userService.findUserByEmail(request.getUsername());

        LoginDTOResponse response = LoginDTOResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .success(true)
                .build();

        // Tạo access token
        String accessToken = securityUtils.accessToken(authentication, response);

        response.setToken(accessToken);

        // Tạo refresh token
        String refreshToken = securityUtils.refreshToken(currentUser.getEmail(), LoginDTOResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .success(true)
                .build());

        // Cập nhật token
        userService.updateUserToken(refreshToken, request.getUsername());

        // set cookies
        ResponseCookie responseCookie = ResponseCookie.from("refresh_Token", refreshToken)
                .httpOnly(true)  // chỉ cho phép cookies chỉ mỗi sever của chúng ta mới sử dụng được
                .secure(true)   // cookies chỉ được sử dụng với https thay vì http, nhưng khi chúng ta sử dụng localhost thì set true hay false đều không có tác dụng
                .path("/") // cho phép cookie được sử dụng trong tất cả dự án của chúng ta
                .maxAge(REFRESH_EXPIRATION) // cookies sau bao lâu sẽ hết hạn, nếu không set mặc định cookies set là session, tắt browser là sẽ hết cookies
                .build();

        // Trả về response
        LoginDTOResponse responseDto = LoginDTOResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .token(accessToken)
                .success(true)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseDto);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<?> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().isPresent() ? SecurityUtils.getCurrentUserLogin().get() : "";
        User currentLogin = userService.findUserByEmail(email);

        new LoginDTOResponse();
        LoginDTOResponse loginDTOResponse;
        if (currentLogin != null) {
            loginDTOResponse = LoginDTOResponse.builder()
                    .id(currentLogin.getId())
                    .email(currentLogin.getEmail())
                    .username(currentLogin.getUsername())
                    .success(true)
                    .token("")
                    .build();
            return ResponseEntity.ok().body(loginDTOResponse);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Current Login");
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh Token")
    public ResponseEntity<String> refreshToken( @CookieValue(name = "refresh_Token") String refresh_Token){
        return ResponseEntity.ok(refresh_Token);
    }
}

