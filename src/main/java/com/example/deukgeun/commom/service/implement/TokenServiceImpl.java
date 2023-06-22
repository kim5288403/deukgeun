package com.example.deukgeun.commom.service.implement;

import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.service.TokenService;
import com.example.deukgeun.trainer.service.implement.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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
public class TokenServiceImpl implements TokenService {
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
   * 초기화 메소드입니다.
   * 보호된 멤버 변수인 secretKey 를 Base64로 인코딩하여 초기화합니다.
   */
  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  /**
   * 인증 토큰을 생성하는 메소드입니다.
   * 사용자 식별자와 역할 정보를 포함한 클레임(claims)을 생성하고,
   * 현재 시간과 토큰 만료 시간을 설정하여 토큰을 생성합니다.
   * 생성된 토큰은 시크릿 키를 사용하여 서명되어 반환됩니다.
   *
   * @param userPk 사용자 식별자
   * @param roles  사용자 역할 정보
   * @return 생성된 인증 토큰
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
   * 리프레시 토큰을 생성하는 메소드입니다.
   * 사용자 식별자를 포함하지 않고 역할 정보만을 포함한 클레임(claims)을 생성하고,
   * 현재 시간과 리프레시 토큰의 만료 시간을 설정하여 토큰을 생성합니다.
   * 생성된 토큰은 시크릿 키를 사용하여 서명되어 반환됩니다.
   *
   * @param userPk 사용자 이메일
   * @param roles 사용자 역할 정보
   * @return 생성된 리프레시 토큰
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
   * HTTP 응답 헤더에 인증 토큰을 설정하는 메소드입니다.
   * 인증 토큰을 Bearer 스키마와 함께 "Authorization" 헤더에 설정하여
   * 클라이언트에게 전송됩니다.
   *
   * @param response   HTTP 응답 객체
   * @param authToken  설정할 인증 토큰
   */
  public void setHeaderAuthToken(HttpServletResponse response, String authToken) {
    response.setHeader("Authorization", "Bearer " + authToken);
  }

  /**
   * HTTP 응답 헤더에 역할 정보를 설정하는 메소드입니다.
   * 역할 정보를 "role" 헤더에 설정하여 클라이언트에게 전송됩니다.
   *
   * @param response  HTTP 응답 객체
   * @param role      설정할 역할 정보
   */
  public void setHeaderRole(HttpServletResponse response, String role) {
    response.setHeader("role", role);
  }

  /**
   * 토큰을 사용하여 인증(Authentication) 객체를 생성하는 메소드입니다.
   * 토큰을 이용하여 사용자 정보를 조회한 후, UserDetails 객체를 생성하여 인증에 활용합니다.
   * 생성된 인증 객체는 UsernamePasswordAuthenticationToken으로 반환됩니다.
   *
   * @param token 토큰 값
   * @return 인증(Authentication) 객체
   */
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = userDetailService.loadUserByUsername(getUserPk(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  /**
   * 토큰을 이용하여 사용자 식별자(User PK)를 추출하는 메소드입니다.
   * 주어진 토큰을 파싱하여 페이로드(claims)에서 사용자 식별자를 추출합니다.
   *
   * @param token 토큰 값
   * @return 사용자 식별자(User PK)
   */
  public String getUserPk(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  /**
   * 토큰을 이용하여 사용자 역할(Role)을 추출하는 메소드입니다.
   * 주어진 토큰을 파싱하여 페이로드(claims)에서 사용자 역할을 추출합니다.
   *
   * @param token 토큰 값
   * @return 사용자 역할(Role)
   */
  public String getUserRole(String token) {
    return (String) Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles");
  }

  /**
   * 주어진 토큰의 유효성을 검사하는 메소드입니다.
   * 주어진 토큰을 파싱하여 시그니처를 검증하고, 만료일자를 확인하여 유효한 토큰인지 판단합니다.
   *
   * @param token 검사할 토큰
   * @return 유효한 토큰인 경우 true, 그렇지 않은 경우 false를 반환합니다.
   */
  public boolean validateToken(String token) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 주어진 이메일을 기반으로 인증 토큰과 리프레시 토큰을 생성하고, 응답 헤더에 인증 토큰을 설정합니다.
   * 또한, 생성된 토큰 정보를 데이터베이스에 저장합니다.
   *
   * @param email    토큰을 생성할 이메일
   * @param response HttpServletResponse 객체
   * @return 생성된 인증 토큰
   */
  public String setToken(String email, HttpServletResponse response) {
    // 인증 토큰 생성
    String authToken = createAuthToken(email, role);

    // 응답 헤더에 인증 토큰 설정
    setHeaderAuthToken(response, authToken);
    
    // 응답 헤더에 role 설정
    setHeaderRole(response, role);

    // 리프레시 토큰 생성
    String refreshToken = createRefreshToken(email, role);

    // 토큰 정보 데이터베이스에 저장
    createToken(authToken, refreshToken);

    return authToken;
  }

  /**
   * 주어진 인증 토큰과 리프레시 토큰을 기반으로 토큰 객체를 생성하고, 데이터베이스에 저장합니다.
   *
   * @param authToken    생성할 인증 토큰
   * @param refreshToken 생성할 리프레시 토큰
   */
  public void createToken(String authToken, String refreshToken) {
    Token token = Token
            .builder()
            .authToken(authToken)
            .refreshToken(refreshToken)
            .build();

    tokenRepository.save(token);
  }

  /**
   * 주어진 인증 토큰을 사용하여 토큰을 데이터베이스에서 삭제합니다.
   *
   * @param authToken 삭제할 인증 토큰
   */
  @CacheEvict(value = "token", key = "#authToken", cacheManager = "projectCacheManager")
  public void deleteToken(String authToken) {
    tokenRepository.deleteByAuthToken(authToken);
  }

  /**
   * 주어진 인증 토큰을 새로운 인증 토큰으로 업데이트합니다.
   *
   * @param authToken     업데이트할 기존 인증 토큰
   * @param newAuthToken  새로운 인증 토큰
   */
  @CachePut(value = "token", key = "#authToken", cacheManager = "projectCacheManager", unless="#result == null")
  public void updateAuthToken(String authToken, String newAuthToken) {
    Token saveToken = tokenRepository.findByAuthToken(authToken).orElse(null);
    assert saveToken != null;
    saveToken.setAuthToken(newAuthToken);
    tokenRepository.save(saveToken);
  }

  /**
   * 주어진 인증 토큰을 사용하여 토큰을 조회합니다.
   *
   * @param authToken  조회할 인증 토큰
   * @return 조회된 토큰 객체, 인증 토큰이 없는 경우 null 을 반환합니다.
   */
//  @Cacheable(value = "token", key = "#authToken", cacheManager = "projectCacheManager", unless = "#result == null")
  public Token findByAuthToken(String authToken) {
    return tokenRepository.findByAuthToken(authToken).orElse(null);
  }

  /**
   * 주어진 인증 토큰을 사용하여 해당 토큰의 리프레시 토큰을 조회합니다.
   *
   * @param authToken  조회할 인증 토큰
   * @return 조회된 리프레시 토큰 값, 인증 토큰이 없는 경우 null 을 반환합니다.
   */
  public String getRefreshTokenByAuthToken(String authToken) {
    // 주어진 인증 토큰을 사용하여 토큰을 조회합니다.
    Token token = findByAuthToken(authToken);
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

  /**
   * HttpServletRequest 에서 Authorization 헤더를 해석하여 인증 토큰을 반환합니다.
   *
   * @param request HttpServletRequest 객체
   * @return 추출된 인증 토큰
   */
  public String resolveAuthToken(HttpServletRequest request) {
    return request.getHeader("Authorization").replace("Bearer ", "");
  }
}
