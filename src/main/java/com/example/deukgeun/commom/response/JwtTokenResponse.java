package com.example.deukgeun.commom.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtTokenResponse {
  private String authToken;
  private String refreshTokne;
}
