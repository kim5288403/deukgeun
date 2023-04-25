package com.example.deukgeun.commom.service.implement;

import javax.servlet.http.HttpServletResponse;
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
   * Jwt 토큰에서 pk 추출
   * @param authToken
   * @return
   */
  public String getUserPk(String authToken) {
    return jwtProvider.getUserPk(authToken);
  }

  public String setCreateToken(String email, HttpServletResponse response) {
    String authToken = jwtProvider.createAuthToken(email, role);
    String refreshToken = jwtProvider.createRefreshToken(email, role);
    
    jwtProvider.setHeaderAccessToken(response, authToken);
    Token token = TokenRequest.create(authToken, refreshToken);
    
    createToken(token);
    
    return authToken;
  }
  
  public void createToken(Token token) {
    tokenRepository.save(token);
  }
  
  public void deleteToken(String authToken) {
    tokenRepository.deleteByAuthToken(authToken);
  }
  
  public void updateAuthToken(String authToken, String newAuthToken) {
    tokenRepository.updateAuthToken(authToken, newAuthToken);
  }
  
  public Token findByAuthToken(String authToken) {
    return tokenRepository.findByAuthToken(authToken).orElse(null);
  }
  
  //저장된 refreshToken 가져옵니다.  
  public String getRefreshToken(String authToken) {
    Token refreshToken = findByAuthToken(authToken);
    return refreshToken != null ? refreshToken.getRefreshToken() : null;
  }
}
