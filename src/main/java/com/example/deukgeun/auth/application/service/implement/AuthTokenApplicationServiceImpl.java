package com.example.deukgeun.auth.application.service.implement;

import com.example.deukgeun.auth.application.service.AuthTokenApplicationService;
import com.example.deukgeun.auth.domain.model.entity.AuthToken;
import com.example.deukgeun.auth.domain.service.implement.AuthTokenDomainServiceImpl;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerDetailServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class AuthTokenApplicationServiceImpl implements AuthTokenApplicationService {
    private final AuthTokenDomainServiceImpl authTokenDomainService;
    private final TrainerDetailServiceImpl trainerDetailService;

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;
    @Value("${jwt.refreshTokenTime}")
    private long refreshTokenTime;
    @Value("${deukgeun.role.trainer}")
    private String trainerRole;
    @Value("${deukgeun.role.member}")
    private String memberRole;


    /**
     * 초기화 메소드입니다.
     * 보호된 멤버 변수인 secretKey 를 Base64로 인코딩하여 초기화합니다.
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    @Override
    public void createToken(String authToken, String refreshToken) {
        authTokenDomainService.createToken(authToken, refreshToken);
    }

    @Override
    public String createAuthToken(String userPk, String roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String createRefreshToken(String userPk, String roles) {
        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    @CacheEvict(value = "token", key = "#authToken", cacheManager = "projectCacheManager")
    public void deleteByAuthToken(String authToken) {
        authTokenDomainService.deleteByAuthToken(authToken);
    }


    @Override
    @Cacheable(value = "token", key = "#authToken", cacheManager = "projectCacheManager", unless = "#result == null")
    public AuthToken findByAuthToken(String authToken) {
        return authTokenDomainService.findByAuthToken(authToken);
    }


    @Override
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    @Override
    public String getUserRole(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles");
    }

    @Override
    public Authentication getAuthentication(String token, String role) {
        String userPk = getUserPk(token);
        UserDetails userDetails = null;

        if (role.equals(trainerRole)) {
            userDetails = trainerDetailService.loadUserByUsername(userPk);
        } else if (role.equals(memberRole)) {
            userDetails = authTokenDomainService.loadUserByUsername(userPk);
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String getRefreshTokenByAuthToken(String authToken) {
        // 주어진 인증 토큰을 사용하여 토큰을 조회합니다.
        AuthToken token = authTokenDomainService.findByAuthToken(authToken);
        String refreshToken;

        // 조회된 토큰이 있는 경우 해당 토큰의 리프레시 토큰 값을 반환합니다.
        // 인증 토큰이 없는 경우 null 을 반환합니다.
        if (token != null) {
            refreshToken = token.getRefreshToken();
        } else {
            refreshToken = null;
        }

        return refreshToken;
    }

    @Override
    public String resolveAuthToken(HttpServletRequest request) {
        return request.getHeader("Authorization").replace("Bearer ", "");
    }

    @Override
    public String setToken(String email, HttpServletResponse response, String role) {
        // 인증 토큰 생성
        String authToken = createAuthToken(email, role);

        // 응답 헤더에 인증 토큰 설정
        setHeaderAuthToken(response, authToken);

        // 응답 헤더에 role 설정
        setHeaderRole(response, role);

        // 리프레시 토큰 생성
        String refreshToken = createRefreshToken(email, role);

        // 토큰 정보 데이터베이스에 저장
        authTokenDomainService.createToken(authToken, refreshToken);

        return authToken;
    }

    @Override
    public void setHeaderRole(HttpServletResponse response, String role) {
        response.setHeader("role", role);
    }


    @Override
    public void setHeaderAuthToken(HttpServletResponse response, String authToken) {
        response.setHeader("Authorization", "Bearer " + authToken);
    }

    @Override
    @CachePut(value = "token", key = "#authToken", cacheManager = "projectCacheManager", unless = "#result == null")
    public void updateAuthToken(String authToken, String newAuthToken) {
        authTokenDomainService.updateAuthToken(authToken, newAuthToken);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
