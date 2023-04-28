package com.example.deukgeun.global.provider;

import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.deukgeun.trainer.service.implement.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;
    @Value("${jwt.refreshTokenTime}")
    private long refreshTokenTime;

    private final UserDetailServiceImpl userDetailService;

    /**
     * 빈이 생성될 때 자동으로 객체에 secretKey 를  Base64 encoding 된 값으로 초기화
     *
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    /**
     * auth 토큰 생성
     * 파라미터로 Claims 에 담아 jwt 형식에 토큰 생성
     *
     * @param userPk = email
     * @param roles = trainer or user
     * @return jwt 형식에 인증 토큰
     */
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

    /**
     * refresh 토큰 생성
     * 파라미터로 Claims 에 담아 jwt 형식에 토큰 생성
     *
     * @param userPk = email
     * @param roles  = trainer or user
     * @return jwt 형식에 refreshToken
     */
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

    /**
     * auth 토큰 response 헤더에 설정
     *
     * @param response HttpServletResponse
     * @param authToken 인증 토큰
     */
    public void setHeaderAccessToken(HttpServletResponse response, String authToken) {
        response.setHeader("Authorization", "Bearer " + authToken);
    }

    /**
     * refresh 토큰 response 헤더에 설정
     *
     * @param response HttpServletResponse
     * @param refreshToken refreshToken 인증 토큰
     */
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("RefreshToken", "Bearer " + refreshToken);
    }

    /**
     * role response 헤더에 설정
     *
     * @param response HttpServletResponse
     * @param role = trainer or user
     */
    public void setHeaderRole(HttpServletResponse response, String role) {
        response.setHeader("role", role);
    }

    /**
     * JWT 에서 유저 정보 조회
     *
     * @param token 인증 토큰
     * @return Authentication 조회된 유저 정보
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * JWT 에서 UserPk 추출
     *
     * @param token 인증 토큰
     * @return 추출된 UserPk
     */
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


    /**
     * JWT 에서 UserRole 추출
     *
     * @param token 인증 토큰
     * @return 추출된 UserRole
     */
    public String getUserRole(String token) {
        return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles");
    }

    /**
     * request Header 에서 authToken 추출
     *
     * @param request authToken 추출을 위한 파라미터
     * @return 추출된 authToken
     */
    public String resolveAuthToken(HttpServletRequest request) {
        return request.getHeader("Authorization").replace("Bearer ", "");
    }

    /**
     * 토큰의 유효성 검사
     *
     * @param jwtToken
     * @return 유효성 검사 결과
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
