package com.example.deukgeun.authToken.application.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
  private String authToken;
  
  private String role;
}
