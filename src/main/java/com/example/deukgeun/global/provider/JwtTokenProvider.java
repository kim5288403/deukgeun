package com.example.deukgeun.global.provider;

import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
  private String secretKey = "myprojectsecret";

  // 토큰 유효시간
  private long tokenValidTime = 30 * 60 * 1000L;
  private final UserServiceImpl userService;

  @PostConstruct
  protected void init() {
      secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  // JWT 토큰 생성 
  public String createToken(String userPk, String roles) {
      Claims claims = Jwts.claims().setSubject(userPk);
      claims.put("roles", roles);
      Date now = new Date();
      return Jwts.builder()
              .setClaims(claims)
              .setIssuedAt(now)
              .setExpiration(new Date(now.getTime() + tokenValidTime))
              .signWith(SignatureAlgorithm.HS256, secretKey)
              .compact();
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
  
  //Request의 Header에서 token 값을 가져옵니다.
  public String resolveToken(HttpServletRequest request) {
      return request.getHeader("Authorization");
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
