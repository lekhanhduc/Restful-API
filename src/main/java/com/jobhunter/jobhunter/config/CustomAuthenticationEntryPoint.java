package com.jobhunter.jobhunter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobhunter.jobhunter.model.CustomErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String INVALID_TOKEN_ERROR_CODE = "invalid_token";
    private static final String INVALID_TOKEN_MESSAGE = "Token is invalid or expired";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = extractErrorMessage(authException);
        CustomErrorResponse customErrorResponse = createCustomErrorResponse(message);

        response.getWriter().write(objectMapper.writeValueAsString(customErrorResponse));
    }

    private String extractErrorMessage(AuthenticationException authException) {
        if (authException instanceof OAuth2AuthenticationException) {
            OAuth2Error error = ((OAuth2AuthenticationException) authException).getError();
            if (INVALID_TOKEN_ERROR_CODE.equals(error.getErrorCode())) {
                return INVALID_TOKEN_MESSAGE;
            }
            return error.getDescription() != null ? error.getDescription() : "Authentication error";
        }
        return "Authentication error";
    }

    private CustomErrorResponse createCustomErrorResponse(String message) {
        return CustomErrorResponse.builder()
                .message(message)
                .timestamp(new Date())
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED))
                .build();
    }
}