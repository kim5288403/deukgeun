package com.example.deukgeun.commom.service.implement;

import org.springframework.stereotype.Service;
import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.service.JwtService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService{
  
  private final TokenRepository tokenRepository;
  
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
}
