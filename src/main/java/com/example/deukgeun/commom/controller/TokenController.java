package com.example.deukgeun.commom.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
  
  private final TokenServiceImpl tokenService;

  /**
   * 로그아웃을 수행합니다.
   *
   * @param request HTTP 요청 객체
   * @return ResponseEntity<?> 응답 엔티티
   */
  @RequestMapping(method = RequestMethod.GET, path = "/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    
    String authToken = tokenService.resolveAuthToken(request);
    tokenService.deleteToken(authToken);
    
    return RestResponseUtil
        .ok("로그아웃 성공 했습니다.", null);
  }

  /**
   * 사용자의 이메일 주소를 조회합니다.
   *
   * @param request HTTP 요청 객체
   * @return ResponseEntity<?> 응답 엔티티
   */
  @RequestMapping(method = RequestMethod.GET, path = "/pk")
  public ResponseEntity<?> getUserPK(HttpServletRequest request) {
    String authToken = tokenService.resolveAuthToken(request);
    String email = tokenService.getUserPk(authToken);

    return RestResponseUtil.ok("이메일 조회 성공했습니다.", email);
  }

}
