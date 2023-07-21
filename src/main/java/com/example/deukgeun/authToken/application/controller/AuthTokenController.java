package com.example.deukgeun.authToken.application.controller;

import com.example.deukgeun.authToken.application.dto.request.LoginRequest;
import com.example.deukgeun.authToken.application.dto.response.LoginResponse;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.global.util.RestResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class AuthTokenController {
  
  private final AuthTokenApplicationService authTokenApplicationService;

  /**
   * 로그인을 처리합니다.
   *
   * @param request         로그인 요청 객체
   * @param bindingResult   유효성 검사 결과 객체
   * @param response        HTTP 응답 객체
   * @return ResponseEntity 객체
   */
  @RequestMapping(method = RequestMethod.POST, path = "/login")
  public ResponseEntity<?> login(@Valid LoginRequest request, BindingResult bindingResult, HttpServletResponse response) {
    // 사용자 로그인 처리
    HashMap<String, String> loginData =  authTokenApplicationService.getLoginData(request.getLoginType(), request.getEmail());
    PasswordEncoderUtil.isPasswordMatches(request.getPassword(), loginData.get("matchPassword"));

    // JWT 토큰 생성 및 설정
    String authToken = authTokenApplicationService.setToken(request.getEmail(), response, loginData.get("role"));

    // 로그인 응답 객체 생성
    LoginResponse loginResponse = LoginResponse
            .builder()
            .authToken(authToken)
            .role(loginData.get("role"))
            .build();

    return RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);
  }

  /**
   * 로그아웃을 수행합니다.
   *
   * @param request HTTP 요청 객체
   * @return ResponseEntity<?> 응답 엔티티
   */
  @RequestMapping(method = RequestMethod.GET, path = "/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    String authToken = authTokenApplicationService.resolveAuthToken(request);
    authTokenApplicationService.deleteByAuthToken(authToken);

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
    String authToken = authTokenApplicationService.resolveAuthToken(request);
    String email = authTokenApplicationService.getUserPk(authToken);

    return RestResponseUtil.ok("이메일 조회 성공했습니다.", email);
  }

}
