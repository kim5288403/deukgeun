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
   * 로그아웃
   * authToken delete
   *
   * @param request authToken 추출을 위한 파라미터
   * @return code 200, message success
   */
  @RequestMapping(method = RequestMethod.GET, path = "/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    
    String authToken = jwtService.resolveAuthToken(request);
    jwtService.deleteToken(authToken);
    
    return RestResponseUtil
        .ok("로그아웃 성공 했습니다.", null);
  }

  /**
   * 트레이너 이메일 가져오기
   * authToken 에서 email 추출하기
   *
   * @param request authToken 추출을 위한 파라미터
   * @return authToken 에서 추출된 email 데이터
   */
  @RequestMapping(method = RequestMethod.GET, path = "/pk")
  public ResponseEntity<?> getUserPK(HttpServletRequest request) {
    String authToken = jwtService.resolveAuthToken(request);
    String email = jwtService.getUserPk(authToken);

    return RestResponseUtil.ok("이메일 조회 성공했습니다.", email);
  }

}
