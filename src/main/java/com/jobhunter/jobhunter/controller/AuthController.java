package com.jobhunter.jobhunter.controller;

import com.jobhunter.jobhunter.annotation.ApiMessage;
import com.jobhunter.jobhunter.dto.request.LoginDTORequest;
import com.jobhunter.jobhunter.dto.response.LoginDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.model.ResourceNotFoundException;
import com.jobhunter.jobhunter.service.UserService;
import com.jobhunter.jobhunter.utils.AuthenticationUtils;
import com.jobhunter.jobhunter.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import static com.jobhunter.jobhunter.utils.UserMapper.mapToDTOResponse;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationUtils authenticationUtils;
    private final SecurityUtils securityUtils;
    private final UserService userService;

    @PostMapping("/auth/login")
    // API này mục đích giúp frontend lấy được access_Token --> lấy token từ phía sever về frontend
    public ResponseEntity<LoginDTOResponse> login(@Valid @RequestBody LoginDTORequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password must not be null or empty");
        }

        // Xác thực người dùng
        Authentication authentication = authenticationUtils.authenticateUser(request.getUsername(), request.getPassword());

        // Lấy thông tin người dùng hiện tại
        User currentUser = userService.findUserByEmail(request.getUsername());

        LoginDTOResponse response = LoginDTOResponse.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .username(currentUser.getUsername())
                .success(true)
                .build();

        // create Token
        String accessToken = securityUtils.accessToken(authentication.getName(), response);
        response.setToken(accessToken);

        String refreshToken = authenticationUtils.createAndSaveRefreshToken(currentUser);

        ResponseCookie responseCookie = authenticationUtils.createResponseCookie(refreshToken);

        LoginDTOResponse responseDto = authenticationUtils.mapToDTOResponseWithToken(currentUser, accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseDto);
    }

    @GetMapping("/auth/account")
    public ResponseEntity<LoginDTOResponse> getAccount() {
        String email = SecurityUtils.getCurrentUserLogin().orElse("");
        User currentLogin = userService.findUserByEmail(email);

        if (currentLogin != null) {
            return ResponseEntity.ok().body(mapToDTOResponse(currentLogin));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Get User by refresh Token")
    public ResponseEntity<LoginDTOResponse> refreshToken(@CookieValue(name = "refresh_Token", required = false) String refresh_Token) {
        // Kiểm tra xem cookie có tồn tại không
        if (refresh_Token == null || refresh_Token.isEmpty()) {
            throw new IllegalArgumentException("Refresh token is missing or empty");
        }

        Jwt decodeToken = securityUtils.checkRefreshToken(refresh_Token);
        String email = decodeToken.getSubject();

        User currentUser = userService.findByRefreshTokenAndEmail(refresh_Token, email);
        if (currentUser == null) {
            throw new ResourceNotFoundException("User not found");
        }

        String accessToken = securityUtils.accessToken(email, mapToDTOResponse(currentUser));
        String newRefreshToken = authenticationUtils.createAndSaveRefreshToken(currentUser);
        ResponseCookie responseCookie = authenticationUtils.createResponseCookie(newRefreshToken);

        LoginDTOResponse responseDto = authenticationUtils.mapToDTOResponseWithToken(currentUser, accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(responseDto);
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout() {
        String currentLogin = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        // Nếu người dùng hiện tại không null, đặt lại refresh token và cập nhật trong cơ sở dữ liệu
        if (currentLogin != null) {
            User user = userService.findUserByEmail(currentLogin);
            userService.updateUserToken(null, user.getEmail());
        }

        // Xóa cookie refresh token
        ResponseCookie responseCookie = authenticationUtils.removeCookies();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString()).build();
    }
}