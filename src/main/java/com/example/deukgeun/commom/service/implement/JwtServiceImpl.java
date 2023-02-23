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
  
  public void createToken(Token refreshToken) {
    tokenRepository.save(refreshToken);
  }
  
  public void deleteToken(String authToken) {
    tokenRepository.deleteByAuthToken(authToken);
  }
  
  public Token findByAuthToken(String authToken) {
    return tokenRepository.findByAuthToken(authToken).orElse(null);
  }
}
