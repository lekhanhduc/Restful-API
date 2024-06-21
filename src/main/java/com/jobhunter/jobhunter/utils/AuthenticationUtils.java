package com.jobhunter.jobhunter.utils;

import com.jobhunter.jobhunter.dto.response.LoginDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.jobhunter.jobhunter.utils.UserMapper.mapToDTOResponse;

@Service
@RequiredArgsConstructor
public class AuthenticationUtils {

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long REFRESH_EXPIRATION;

    private final AuthenticationManagerBuilder authenticationManagerBuilder; // add 'final'

    private final UserService userService;
    private final SecurityUtils securityUtils;

    public Authentication authenticateUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public ResponseCookie createResponseCookie(String refreshToken) {
        return ResponseCookie.from("refresh_Token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_EXPIRATION)
                .build();
    }

    public ResponseCookie removeCookies() {
        return ResponseCookie.from("refresh_Token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
    }

    public String createAndSaveRefreshToken(User currentUser) {
        String refreshToken = securityUtils.refreshToken(currentUser.getEmail(), mapToDTOResponse(currentUser));
        userService.updateUserToken(refreshToken, currentUser.getEmail());
        return refreshToken;
    }

    public LoginDTOResponse mapToDTOResponseWithToken(User user, String token) {
        return LoginDTOResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .token(token)
                .success(true)
                .build();
    }
}