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
import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.request.TokenRequest;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
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
  
  private final UserServiceImpl userService;
  private final JwtServiceImpl jwtService;

  @PostConstruct
  protected void init() {
      secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  // auth 토큰 생성 
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
  
  // refresh 토큰 생성 
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
  
  public void createTokenEntity(String authToken, String refreshToken) {
    jwtService.createToken(TokenRequest.create(authToken, refreshToken));
  }
  
  public void deleteTokenEntity(String authToken) {
    jwtService.deleteToken(authToken);
  }
  
  public void updateAuthToken(String authToken, String newAuthToken) {
    jwtService.updateAuthToken(authToken, newAuthToken);
  }
  
  // auth 토큰 헤더 설정
  public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
      response.setHeader("Authorization", "Bearer " + accessToken);
  }

  // refresh 토큰 헤더 설정
  public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
      response.setHeader("RefreshToken", "Bearer " + refreshToken);
  }
  
  // role 헤더 설정
  public void setHeaderRole(HttpServletResponse response, String role) {
      response.setHeader("role", role);
  }
  
  // JWT 토큰에서 인증 정보 조회
  public Authentication getAuthentication(String token) {
      UserDetails userDetails = userService.loadUserByUsername(this.getUserPk(token));
      return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // 토큰에서 회원 정보 추출
  public String getUserPk(String token) {
      return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }
  
  // 토큰에서 회원 정보 추출
  public String getUserRole(String token) {
    return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles");
  }
  
  //Request의 Header에서 auth token 값을 가져옵니다.
  public String resolveAuthToken(HttpServletRequest request) {
      return request.getHeader("Authorization").replace("Bearer ", "");
  }
  
  //저장된 refreshToken 가져옵니다.  
  public String getRefreshToken(String authToken) {
    Token refreshToken = jwtService.findByAuthToken(authToken);
    return refreshToken != null ? refreshToken.getRefreshToken() : null;
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String jwtToken) {
      try {
          Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
          return !claims.getBody().getExpiration().before(new Date());
      } catch (Exception e) {
          return false;
      }
  }
}
