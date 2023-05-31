package com.example.deukgeun.commom.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jwt")
@RequiredArgsConstructor
public class JwtController {
  
  private final JwtServiceImpl jwtService;

  /**
   * authToken check 용
   */
  @RequestMapping(method = RequestMethod.POST, path = "/check")
  public void check() {
  }

  /**
   * 로그아웃을 수행합니다.
   *
   * @param request HTTP 요청 객체
   * @return ResponseEntity<?> 응답 엔티티
   */
  @RequestMapping(method = RequestMethod.GET, path = "/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    
    String authToken = jwtService.resolveAuthToken(request);
    jwtService.deleteToken(authToken);
    
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
    String authToken = jwtService.resolveAuthToken(request);
    String email = jwtService.getUserPk(authToken);

    return RestResponseUtil.ok("이메일 조회 성공했습니다.", email);
  }

}
