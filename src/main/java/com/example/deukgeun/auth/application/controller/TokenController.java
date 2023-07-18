package com.example.deukgeun.auth.application.controller;

import com.example.deukgeun.auth.application.dto.request.LoginRequest;
import com.example.deukgeun.auth.application.dto.response.LoginResponse;
import com.example.deukgeun.auth.application.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.infrastructure.persistence.MemberServiceImpl;
import com.example.deukgeun.trainer.domain.entity.Trainer;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class TokenController {
  
  private final TokenServiceImpl tokenService;
  private final TrainerServiceImpl trainerService;
  private final MemberServiceImpl memberService;

  @Value("${deukgeun.role.trainer}")
  private String trainerRole;

  @Value("${deukgeun.role.member}")
  private String memberRole;

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
    String role = null;
    String matchPassword = null;

    if (request.getLoginType().equals("trainer")) {
      Trainer trainer = trainerService.getByEmail(request.getEmail());
      matchPassword = trainer.getPassword();
      role = trainerRole;
    } else if (request.getLoginType().equals("member")) {
      Member member = memberService.getByEmail(request.getEmail());
      matchPassword = member.getPassword();
      role = memberRole;
    }

    PasswordEncoderUtil.isPasswordMatches(request.getPassword(), matchPassword);

    // JWT 토큰 생성 및 설정
    String authToken = tokenService.setToken(request.getEmail(), response, role);

    // 로그인 응답 객체 생성
    LoginResponse loginResponse = LoginResponse
            .builder()
            .authToken(authToken)
            .role(role)
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
