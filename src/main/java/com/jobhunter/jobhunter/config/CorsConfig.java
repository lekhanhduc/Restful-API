package com.jobhunter.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4173", "http://localhost:5173")); // cổng có thể kết nối với backend
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "GET", "PUT", "DELETE", "OPTIONS")); // các method có thể kết nối với backend
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "x-no-retry")); // header được phép gửi lên gì ?
        corsConfiguration.setAllowCredentials(true); // gửi kèm cookies hay không ?
        corsConfiguration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
}
