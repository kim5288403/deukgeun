package com.example.deukgeun.auth.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private String authToken;
  
  private String role;
}
