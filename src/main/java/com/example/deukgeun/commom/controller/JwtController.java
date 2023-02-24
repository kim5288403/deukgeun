package com.example.deukgeun.commom.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.MessageResponse;
import com.example.deukgeun.global.provider.JwtProvider;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {
  
  private final JwtProvider jwtProvider;
  
  @RequestMapping(method = RequestMethod.POST, path = "/check")
  public void check() {
  }
  
  @RequestMapping(method = RequestMethod.GET, path = "/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    
    String authToken = request.getHeader("Authorization").replace("Bearer ", "");
    jwtProvider.deleteTokenEntity(authToken);
    
    MessageResponse response = MessageResponse
        .builder()
        .data(null)
        .message("로그아웃 성공 했습니다.")
        .code(StatusEnum.OK.getCode())
        .status(StatusEnum.OK.getStatus())
        .build();

    return ResponseEntity
        .ok()
        .body(response);
  }
}
