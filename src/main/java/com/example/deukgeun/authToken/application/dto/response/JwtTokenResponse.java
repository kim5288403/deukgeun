package com.example.deukgeun.authToken.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
  private String authToken;
}
