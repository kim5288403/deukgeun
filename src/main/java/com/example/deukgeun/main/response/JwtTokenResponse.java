package com.example.deukgeun.main.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
  private String authToken;
}
