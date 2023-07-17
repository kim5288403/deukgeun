package com.example.deukgeun.auth.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
  private String authToken;
}
