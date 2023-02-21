package com.example.deukgeun.commom.request;

import javax.persistence.Column;
import com.example.deukgeun.commom.entity.Token;
import lombok.Data;

@Data
public class TokenRequest {
  @Column(name = "auth_token")
  private String authToken;
  
  @Column(name = "refresh_token")
  private String refreshToken;
  
  public static Token create(String authToken, String refreshToken) {
    return Token
        .builder()
        .authToken(authToken)
        .refreshToken(refreshToken)
        .build();
  }
}
