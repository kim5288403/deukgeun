package com.example.deukgeun.commom.service.implement;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.deukgeun.trainer.service.implement.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.request.TokenRequest;
import com.example.deukgeun.commom.service.JwtService;
import lombok.RequiredArgsConstructor;

import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService{
  private final TokenRepository tokenRepository;
  private final UserDetailServiceImpl userDetailService;

  @Value("${deukgeun.role.trainer}")
  private String role;
  @Value("${jwt.secretKey}")
  private String secretKey;
  @Value("${jwt.authTokenTime}")
  private long authTokenTime;
  @Value("${jwt.refreshTokenTime}")
  private long refreshTokenTime;


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

  /**
   * header 에 authToken 저장 및 DB에 refreshToken 저장
   *
   * @param email user email
   * @param response HttpServletResponse
   * @return 새로 등록된 인증 토큰 값
   */
  public String setCreateToken(String email, HttpServletResponse response) {
    String authToken = createAuthToken(email, role);
    String refreshToken = createRefreshToken(email, role);
    
    setHeaderAccessToken(response, authToken);
    Token token = TokenRequest.create(authToken, refreshToken);
    
    createToken(token);
    
    return authToken;
  }

  /**
   * DB 토큰 저장
   *
   * @param token authToken + refreshToken
   */
  public void createToken(Token token) {
    tokenRepository.save(token);
  }

  /**
   * DB 토큰 삭제
   * 파라미터에 해당되는 데이터 삭제
   *
   * @param authToken 기존 JWT 데이터
   */
  @CacheEvict(value = "token", key = "#authToken", cacheManager = "projectCacheManager")
  public void deleteToken(String authToken) {
    tokenRepository.deleteByAuthToken(authToken);
  }

  public boolean expireToken(String token) {
    try {
      Claims claims = Jwts.parser()
              .setSigningKey(secretKey)
              .parseClaimsJws(token)
              .getBody();

      Date expirationDate = claims.getExpiration();
      return expirationDate.before(new Date());
    } catch (ExpiredJwtException ex) {
      return true; // 토큰이 이미 만료된 경우
    } catch (Exception ex) {
      return true; // 토큰 파싱이 실패한 경우
    }
  }

  /**
   * DB 토큰 수정
   * 파라미터에 해당되는 데이터 수정
   *
   * @param authToken 기존 JWT 데이터
   * @param newAuthToken 수정을 위한 새로운 JWT 토큰
   */
  @CachePut(value = "token", key = "#authToken", cacheManager = "projectCacheManager")
  public void updateAuthToken(String authToken, String newAuthToken) {
    tokenRepository.updateAuthToken(authToken, newAuthToken);
  }

  /**
   * DB 토큰 데이터 가져오기
   * 파라미터에 해당되는 데이터 가져오기
   *
   * @param authToken 기존 JWT 데이터
   * @return 데이터가 있을 경우 Token data, 없을 경우 null 값 반환
   */
  @Cacheable(value = "token", key = "#authToken", cacheManager = "projectCacheManager")
  public Token findByAuthToken(String authToken) {
    return tokenRepository.findByAuthToken(authToken).orElse(null);
  }

  /**
   * DB 토큰 데이터에서 refreshToken 데이터 추출
   * 파라미터에 해당되는 데이터를 가져와 refreshToken 추출
   *
   * @param authToken 기존 JWT 데이터
   * @return 데이터가 있을 경우 refreshToken, 없을 경우 null;
   */
  public String getRefreshTokenByAuthToken(String authToken) {
    Token token = findByAuthToken(authToken);
    String refreshToken;

    if (token != null) {
      refreshToken = token.getRefreshToken();
    } else {
      refreshToken = null;
    }

    return refreshToken;
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
}
