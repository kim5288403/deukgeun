package com.example.deukgeun.commom.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  private String authToken;
  
  private String role;
}
