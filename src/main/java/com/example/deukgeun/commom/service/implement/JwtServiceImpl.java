package com.example.deukgeun.commom.service.implement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.request.TokenRequest;
import com.example.deukgeun.commom.service.JwtService;
import com.example.deukgeun.global.provider.JwtProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService{
  
  private String role = "trainer";
  
  private final JwtProvider jwtProvider;
  private final TokenRepository tokenRepository;

  /**
   * authToken 토큰에서 pk 추출
   *
   * @param authToken 인증 토큰 값
   * @return  토큰에서 pk 추출된 값
   */
  public String getUserPk(String authToken) {
    return jwtProvider.getUserPk(authToken);
  }

  /**
   * header 에 authToken 저장 및 DB에 refreshToken 저장
   *
   * @param email user email
   * @param response HttpServletResponse
   * @return 새로 등록된 인증 토큰 값
   */
  public String setCreateToken(String email, HttpServletResponse response) {
    String authToken = jwtProvider.createAuthToken(email, role);
    String refreshToken = jwtProvider.createRefreshToken(email, role);
    
    jwtProvider.setHeaderAccessToken(response, authToken);
    Token token = TokenRequest.create(authToken, refreshToken);
    
    createToken(token);
    
    return authToken;
  }

  /**
   * DB 토큰 저장
   *
   * @param token authToken + refreshToken
   */
  @CachePut(value = "getAuthToken", key = "#token.id", cacheManager = "projectCacheManager")
  public void createToken(Token token) {
    tokenRepository.save(token);
  }

  /**
   * DB 토큰 삭제
   * 파라미터에 해당되는 데이터 삭제
   *
   * @param authToken 기존 JWT 데이터
   */
  @CacheEvict(value = "getAuthToken", key = "#authToken", cacheManager = "projectCacheManager")
  public void deleteToken(String authToken) {
    tokenRepository.deleteByAuthToken(authToken);
  }

  /**
   * DB 토큰 수정
   * 파라미터에 해당되는 데이터 수정
   *
   * @param authToken 기존 JWT 데이터
   * @param newAuthToken 수정을 위한 새로운 JWT 토큰
   */
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
  @Cacheable(value = "getAuthToken", key = "#authToken", cacheManager = "projectCacheManager")
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
