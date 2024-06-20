package com.jobhunter.jobhunter.utils;


import com.jobhunter.jobhunter.dto.request.LoginDTOResponse;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUtils {

    private final JwtEncoder jwtEncoder;

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${jwt.base64.secret}")
    private String KEY_SECRET;

    @Value("${jwt.token-validity-in-seconds}")
    private long ACCESSION_EXPIRATION;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long REFRESH_EXPIRATION;


    private SecretKey getSecretKey(){
        byte[] keyBytes = Base64.from(KEY_SECRET).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, JWT_ALGORITHM.getName());
    }

    public void checkRefreshToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
                .withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtils.JWT_ALGORITHM).build();
    }

    public  String accessToken(Authentication authentication, LoginDTOResponse response){
        Instant now = Instant.now();
        Instant validity = now.plus(this.ACCESSION_EXPIRATION, ChronoUnit.SECONDS);

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(authentication.getName()) //subject thường được sử dụng để chỉ ra người dùng hoặc thực thể mà token đại diện
                .issuer("khanhduc.com")
                .issuedAt(now)
                .expiresAt(validity)
                .claim("id", response.getId())
                .claim("username", response.getUsername())
                .claim("email", response.getEmail())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)).getTokenValue();
    }

    public  String refreshToken(String email, LoginDTOResponse response){
        Instant now = Instant.now();
        Instant validity = now.plus(this.REFRESH_EXPIRATION, ChronoUnit.SECONDS);

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(email) //subject thường được sử dụng để chỉ ra người dùng hoặc thực thể mà token đại diện
                .issuer("khanhduc.com")
                .issuedAt(now)
                .expiresAt(validity)
                .claim("id", response.getId())
                .claim("email", response.getEmail())
                .claim("username", response.getUsername())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet)).getTokenValue();
    }


    public static Optional<String> getCurrentUserLogin(){
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(contextHolder.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication){
        if(authentication == null){
            return null;
        }else if(authentication.getPrincipal() instanceof UserDetails springSecurityUser){
            return springSecurityUser.getUsername();
        }else if(authentication.getPrincipal() instanceof Jwt jwt){
            return jwt.getSubject();
        }else if(authentication.getPrincipal() instanceof String s){
            return s;
        }
        return null;
    }

    public static Optional<String> getCurrentUserJwt(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }


}
