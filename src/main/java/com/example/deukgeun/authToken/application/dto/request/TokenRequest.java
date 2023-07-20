package com.example.deukgeun.authToken.application.dto.request;

import javax.persistence.Column;

import lombok.Data;

@Data
public class TokenRequest {
  @Column(name = "auth_token")
  private String authToken;
  
  @Column(name = "refresh_token")
  private String refreshToken;
}
